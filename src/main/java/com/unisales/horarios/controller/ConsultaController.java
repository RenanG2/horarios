package com.unisales.horarios.controller;

import com.unisales.horarios.dto.AgendaProfessorResponse;
import com.unisales.horarios.dto.AgendaTurmaResponse;
import com.unisales.horarios.service.ConsultaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Tag(name = "Consultas", description = "Visualização de horários por professor e por turma")
public class ConsultaController {

    private final ConsultaService service;

    public ConsultaController(ConsultaService service) {
        this.service = service;
    }

    @GetMapping("/professores/{id}/horarios")
    @Operation(summary = "Horários por professor",
            description = "Retorna as disciplinas ministradas, as turmas associadas e os dias/horários das aulas.")
    public AgendaProfessorResponse porProfessor(@PathVariable Long id) {
        return service.agendaDoProfessor(id);
    }

    @GetMapping("/turmas/{id}/horarios")
    @Operation(summary = "Horários por turma",
            description = "Retorna os professores responsáveis, as disciplinas oferecidas e os dias/horários das aulas.")
    public AgendaTurmaResponse porTurma(@PathVariable Long id) {
        return service.agendaDaTurma(id);
    }
}
