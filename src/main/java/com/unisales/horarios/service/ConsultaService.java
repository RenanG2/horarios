package com.unisales.horarios.service;

import com.unisales.horarios.dto.*;
import com.unisales.horarios.model.Horario;
import com.unisales.horarios.repository.HorarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

/** Consultas obrigatórias do enunciado: agenda por professor e agenda por turma. */
@Service
@Transactional(readOnly = true)
public class ConsultaService {

    private static final Comparator<Horario> ORDEM_AGENDA =
            Comparator.comparing(Horario::getDiaSemana).thenComparing(Horario::getHoraInicio);

    private final HorarioRepository horarioRepository;
    private final ProfessorService professorService;
    private final TurmaService turmaService;

    public ConsultaService(HorarioRepository horarioRepository, ProfessorService professorService,
                           TurmaService turmaService) {
        this.horarioRepository = horarioRepository;
        this.professorService = professorService;
        this.turmaService = turmaService;
    }

    public AgendaProfessorResponse agendaDoProfessor(Long professorId) {
        var professor = professorService.buscarEntidade(professorId);
        List<Horario> horarios = horarioRepository.findByProfessorId(professorId).stream()
                .sorted(ORDEM_AGENDA).toList();

        // distinct() funciona porque records implementam equals() por valor
        List<DisciplinaResponse> disciplinas = horarios.stream()
                .map(h -> DisciplinaResponse.from(h.getDisciplina())).distinct().toList();
        List<TurmaResponse> turmas = horarios.stream()
                .map(h -> TurmaResponse.from(h.getTurma())).distinct().toList();
        List<AgendaProfessorResponse.Aula> aulas = horarios.stream()
                .map(h -> new AgendaProfessorResponse.Aula(h.getDisciplina().getNome(), h.getTurma().getNome(),
                        h.getDiaSemana(), h.getHoraInicio(), h.getHoraFim()))
                .toList();

        return new AgendaProfessorResponse(ProfessorResponse.from(professor), disciplinas, turmas, aulas);
    }

    public AgendaTurmaResponse agendaDaTurma(Long turmaId) {
        var turma = turmaService.buscarEntidade(turmaId);
        List<Horario> horarios = horarioRepository.findByTurmaId(turmaId).stream()
                .sorted(ORDEM_AGENDA).toList();

        List<ProfessorResponse> professores = horarios.stream()
                .map(h -> ProfessorResponse.from(h.getProfessor())).distinct().toList();
        List<DisciplinaResponse> disciplinas = horarios.stream()
                .map(h -> DisciplinaResponse.from(h.getDisciplina())).distinct().toList();
        List<AgendaTurmaResponse.Aula> aulas = horarios.stream()
                .map(h -> new AgendaTurmaResponse.Aula(h.getProfessor().getNome(), h.getDisciplina().getNome(),
                        h.getDiaSemana(), h.getHoraInicio(), h.getHoraFim()))
                .toList();

        return new AgendaTurmaResponse(TurmaResponse.from(turma), professores, disciplinas, aulas);
    }
}
