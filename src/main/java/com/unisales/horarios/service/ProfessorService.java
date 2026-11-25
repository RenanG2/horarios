package com.unisales.horarios.service;

import com.unisales.horarios.dto.ProfessorRequest;
import com.unisales.horarios.dto.ProfessorResponse;
import com.unisales.horarios.exception.RecursoNaoEncontradoException;
import com.unisales.horarios.exception.RegraNegocioException;
import com.unisales.horarios.model.Professor;
import com.unisales.horarios.repository.HorarioRepository;
import com.unisales.horarios.repository.ProfessorRepository;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/** Regras de negócio de Professor. O controller só recebe/devolve dados; a lógica fica aqui. */
@Service
@Transactional(readOnly = true)
public class ProfessorService {

    private final ProfessorRepository professorRepository;
    private final HorarioRepository horarioRepository;

    public ProfessorService(ProfessorRepository professorRepository, HorarioRepository horarioRepository) {
        this.professorRepository = professorRepository;
        this.horarioRepository = horarioRepository;
    }

    public List<ProfessorResponse> listar() {
        return professorRepository.findAll(Sort.by("nome")).stream().map(ProfessorResponse::from).toList();
    }

    public ProfessorResponse buscar(Long id) {
        return ProfessorResponse.from(buscarEntidade(id));
    }

    public Professor buscarEntidade(Long id) {
        return professorRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Professor", id));
    }

    @Transactional
    public ProfessorResponse criar(ProfessorRequest req) {
        if (professorRepository.existsByEmail(req.email())) {
            throw new RegraNegocioException("Já existe um professor com este e-mail", HttpStatus.CONFLICT);
        }
        Professor p = new Professor();
        p.setNome(req.nome());
        p.setEmail(req.email());
        return ProfessorResponse.from(professorRepository.save(p));
    }

    @Transactional
    public ProfessorResponse atualizar(Long id, ProfessorRequest req) {
        Professor p = buscarEntidade(id);
        if (professorRepository.existsByEmailAndIdNot(req.email(), id)) {
            throw new RegraNegocioException("Já existe um professor com este e-mail", HttpStatus.CONFLICT);
        }
        p.setNome(req.nome());
        p.setEmail(req.email());
        return ProfessorResponse.from(professorRepository.save(p));
    }

    @Transactional
    public void excluir(Long id) {
        Professor p = buscarEntidade(id);
        if (horarioRepository.existsByProfessorId(id)) {
            throw new RegraNegocioException(
                    "Não é possível excluir: o professor possui horários cadastrados", HttpStatus.CONFLICT);
        }
        professorRepository.delete(p);
    }
}
