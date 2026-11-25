package com.unisales.horarios.controller;

import com.unisales.horarios.dto.TurmaRequest;
import com.unisales.horarios.dto.TurmaResponse;
import com.unisales.horarios.service.TurmaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/turmas")
@Tag(name = "Turmas", description = "Cadastro de turmas")
public class TurmaController {

    private final TurmaService service;

    public TurmaController(TurmaService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Lista todas as turmas")
    public List<TurmaResponse> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca uma turma pelo id")
    public TurmaResponse buscar(@PathVariable Long id) {
        return service.buscar(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastra uma turma")
    public TurmaResponse criar(@Valid @RequestBody TurmaRequest request) {
        return service.criar(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza uma turma")
    public TurmaResponse atualizar(@PathVariable Long id, @Valid @RequestBody TurmaRequest request) {
        return service.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Exclui uma turma (somente se não tiver horários)")
    public void excluir(@PathVariable Long id) {
        service.excluir(id);
    }
}
