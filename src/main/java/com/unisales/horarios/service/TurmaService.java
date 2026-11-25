package com.unisales.horarios.service;

import com.unisales.horarios.dto.TurmaRequest;
import com.unisales.horarios.dto.TurmaResponse;
import com.unisales.horarios.exception.RecursoNaoEncontradoException;
import com.unisales.horarios.exception.RegraNegocioException;
import com.unisales.horarios.model.Turma;
import com.unisales.horarios.repository.HorarioRepository;
import com.unisales.horarios.repository.TurmaRepository;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class TurmaService {

    private final TurmaRepository turmaRepository;
    private final HorarioRepository horarioRepository;

    public TurmaService(TurmaRepository turmaRepository, HorarioRepository horarioRepository) {
        this.turmaRepository = turmaRepository;
        this.horarioRepository = horarioRepository;
    }

    public List<TurmaResponse> listar() {
        return turmaRepository.findAll(Sort.by("nome")).stream().map(TurmaResponse::from).toList();
    }

    public TurmaResponse buscar(Long id) {
        return TurmaResponse.from(buscarEntidade(id));
    }

    public Turma buscarEntidade(Long id) {
        return turmaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Turma", id));
    }

    @Transactional
    public TurmaResponse criar(TurmaRequest req) {
        Turma t = new Turma();
        t.setNome(req.nome());
        t.setSemestre(req.semestre());
        return TurmaResponse.from(turmaRepository.save(t));
    }

    @Transactional
    public TurmaResponse atualizar(Long id, TurmaRequest req) {
        Turma t = buscarEntidade(id);
        t.setNome(req.nome());
        t.setSemestre(req.semestre());
        return TurmaResponse.from(turmaRepository.save(t));
    }

    @Transactional
    public void excluir(Long id) {
        Turma t = buscarEntidade(id);
        if (horarioRepository.existsByTurmaId(id)) {
            throw new RegraNegocioException(
                    "Não é possível excluir: a turma possui horários cadastrados", HttpStatus.CONFLICT);
        }
        turmaRepository.delete(t);
    }
}
