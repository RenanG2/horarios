package com.unisales.horarios.controller;

import com.unisales.horarios.dto.DisciplinaRequest;
import com.unisales.horarios.dto.DisciplinaResponse;
import com.unisales.horarios.service.DisciplinaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/disciplinas")
@Tag(name = "Disciplinas", description = "Cadastro de disciplinas")
public class DisciplinaController {

    private final DisciplinaService service;

    public DisciplinaController(DisciplinaService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Lista todas as disciplinas")
    public List<DisciplinaResponse> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca uma disciplina pelo id")
    public DisciplinaResponse buscar(@PathVariable Long id) {
        return service.buscar(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastra uma disciplina")
    public DisciplinaResponse criar(@Valid @RequestBody DisciplinaRequest request) {
        return service.criar(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza uma disciplina")
    public DisciplinaResponse atualizar(@PathVariable Long id, @Valid @RequestBody DisciplinaRequest request) {
        return service.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Exclui uma disciplina (somente se não tiver horários)")
    public void excluir(@PathVariable Long id) {
        service.excluir(id);
    }
}
