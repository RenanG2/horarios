package com.unisales.horarios.controller;

import com.unisales.horarios.dto.ProfessorRequest;
import com.unisales.horarios.dto.ProfessorResponse;
import com.unisales.horarios.service.ProfessorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/professores")
@Tag(name = "Professores", description = "Cadastro de professores")
public class ProfessorController {

    private final ProfessorService service;

    public ProfessorController(ProfessorService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Lista todos os professores")
    public List<ProfessorResponse> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um professor pelo id")
    public ProfessorResponse buscar(@PathVariable Long id) {
        return service.buscar(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastra um professor")
    public ProfessorResponse criar(@Valid @RequestBody ProfessorRequest request) {
        return service.criar(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um professor")
    public ProfessorResponse atualizar(@PathVariable Long id, @Valid @RequestBody ProfessorRequest request) {
        return service.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Exclui um professor (somente se não tiver horários)")
    public void excluir(@PathVariable Long id) {
        service.excluir(id);
    }
}
