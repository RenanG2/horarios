package com.unisales.horarios.controller;

import com.unisales.horarios.dto.HorarioRequest;
import com.unisales.horarios.dto.HorarioResponse;
import com.unisales.horarios.service.HorarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/horarios")
@Tag(name = "Horários", description = "Cadastro de horários de aula (liga professor, disciplina e turma)")
public class HorarioController {

    private final HorarioService service;

    public HorarioController(HorarioService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Lista todos os horários, ordenados por dia e hora")
    public List<HorarioResponse> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um horário pelo id")
    public HorarioResponse buscar(@PathVariable Long id) {
        return service.buscar(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastra um horário", description = "Retorna 409 se houver conflito para o professor ou para a turma.")
    public HorarioResponse criar(@Valid @RequestBody HorarioRequest request) {
        return service.criar(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um horário")
    public HorarioResponse atualizar(@PathVariable Long id, @Valid @RequestBody HorarioRequest request) {
        return service.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Exclui um horário")
    public void excluir(@PathVariable Long id) {
        service.excluir(id);
    }
}
