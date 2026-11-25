package com.unisales.horarios.service;

import com.unisales.horarios.dto.HorarioRequest;
import com.unisales.horarios.dto.HorarioResponse;
import com.unisales.horarios.exception.RecursoNaoEncontradoException;
import com.unisales.horarios.exception.RegraNegocioException;
import com.unisales.horarios.model.Horario;
import com.unisales.horarios.repository.HorarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

/** Regras de Horário: valida intervalo e impede conflitos (mesmo professor ou mesma turma). */
@Service
@Transactional(readOnly = true)
public class HorarioService {

    private static final Long SEM_ID = -1L;

    private final HorarioRepository horarioRepository;
    private final ProfessorService professorService;
    private final DisciplinaService disciplinaService;
    private final TurmaService turmaService;

    public HorarioService(HorarioRepository horarioRepository, ProfessorService professorService,
                          DisciplinaService disciplinaService, TurmaService turmaService) {
        this.horarioRepository = horarioRepository;
        this.professorService = professorService;
        this.disciplinaService = disciplinaService;
        this.turmaService = turmaService;
    }

    public List<HorarioResponse> listar() {
        return horarioRepository.findAll().stream()
                .sorted(Comparator.comparing(Horario::getDiaSemana).thenComparing(Horario::getHoraInicio))
                .map(HorarioResponse::from)
                .toList();
    }

    public HorarioResponse buscar(Long id) {
        return HorarioResponse.from(buscarEntidade(id));
    }

    private Horario buscarEntidade(Long id) {
        return horarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Horário", id));
    }

    @Transactional
    public HorarioResponse criar(HorarioRequest req) {
        Horario h = new Horario();
        preencher(h, req, SEM_ID);
        return HorarioResponse.from(horarioRepository.save(h));
    }

    @Transactional
    public HorarioResponse atualizar(Long id, HorarioRequest req) {
        Horario h = buscarEntidade(id);
        preencher(h, req, id);
        return HorarioResponse.from(horarioRepository.save(h));
    }

    @Transactional
    public void excluir(Long id) {
        horarioRepository.delete(buscarEntidade(id));
    }

    private void preencher(Horario h, HorarioRequest req, Long idAtual) {
        if (!req.horaFim().isAfter(req.horaInicio())) {
            throw new RegraNegocioException("A hora de fim deve ser posterior à hora de início", HttpStatus.BAD_REQUEST);
        }
        h.setProfessor(professorService.buscarEntidade(req.professorId()));
        h.setDisciplina(disciplinaService.buscarEntidade(req.disciplinaId()));
        h.setTurma(turmaService.buscarEntidade(req.turmaId()));
        h.setDiaSemana(req.diaSemana());
        h.setHoraInicio(req.horaInicio());
        h.setHoraFim(req.horaFim());

        if (horarioRepository.existeConflitoProfessor(req.professorId(), req.diaSemana(),
                req.horaInicio(), req.horaFim(), idAtual)) {
            throw new RegraNegocioException(
                    "Conflito de horário: o professor já tem aula neste dia e intervalo", HttpStatus.CONFLICT);
        }
        if (horarioRepository.existeConflitoTurma(req.turmaId(), req.diaSemana(),
                req.horaInicio(), req.horaFim(), idAtual)) {
            throw new RegraNegocioException(
                    "Conflito de horário: a turma já tem aula neste dia e intervalo", HttpStatus.CONFLICT);
        }
    }
}
