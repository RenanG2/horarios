package com.unisales.horarios.service;

import com.unisales.horarios.dto.DisciplinaRequest;
import com.unisales.horarios.dto.DisciplinaResponse;
import com.unisales.horarios.exception.RecursoNaoEncontradoException;
import com.unisales.horarios.exception.RegraNegocioException;
import com.unisales.horarios.model.Disciplina;
import com.unisales.horarios.repository.DisciplinaRepository;
import com.unisales.horarios.repository.HorarioRepository;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class DisciplinaService {

    private final DisciplinaRepository disciplinaRepository;
    private final HorarioRepository horarioRepository;

    public DisciplinaService(DisciplinaRepository disciplinaRepository, HorarioRepository horarioRepository) {
        this.disciplinaRepository = disciplinaRepository;
        this.horarioRepository = horarioRepository;
    }

    public List<DisciplinaResponse> listar() {
        return disciplinaRepository.findAll(Sort.by("nome")).stream().map(DisciplinaResponse::from).toList();
    }

    public DisciplinaResponse buscar(Long id) {
        return DisciplinaResponse.from(buscarEntidade(id));
    }

    public Disciplina buscarEntidade(Long id) {
        return disciplinaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Disciplina", id));
    }

    @Transactional
    public DisciplinaResponse criar(DisciplinaRequest req) {
        Disciplina d = new Disciplina();
        d.setNome(req.nome());
        d.setCargaHoraria(req.cargaHoraria());
        return DisciplinaResponse.from(disciplinaRepository.save(d));
    }

    @Transactional
    public DisciplinaResponse atualizar(Long id, DisciplinaRequest req) {
        Disciplina d = buscarEntidade(id);
        d.setNome(req.nome());
        d.setCargaHoraria(req.cargaHoraria());
        return DisciplinaResponse.from(disciplinaRepository.save(d));
    }

    @Transactional
    public void excluir(Long id) {
        Disciplina d = buscarEntidade(id);
        if (horarioRepository.existsByDisciplinaId(id)) {
            throw new RegraNegocioException(
                    "Não é possível excluir: a disciplina possui horários cadastrados", HttpStatus.CONFLICT);
        }
        disciplinaRepository.delete(d);
    }
}
