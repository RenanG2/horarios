package com.unisales.horarios.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

/** Resultado da consulta "horários por professor". */
public record AgendaProfessorResponse(
        ProfessorResponse professor,
        List<DisciplinaResponse> disciplinas,
        List<TurmaResponse> turmas,
        List<Aula> aulas) {

    public record Aula(
            String disciplina,
            String turma,
            DayOfWeek diaSemana,
            @Schema(type = "string", example = "19:00") @JsonFormat(pattern = "HH:mm") LocalTime horaInicio,
            @Schema(type = "string", example = "20:40") @JsonFormat(pattern = "HH:mm") LocalTime horaFim) {
    }
}
