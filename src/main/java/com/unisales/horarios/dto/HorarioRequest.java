package com.unisales.horarios.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.time.LocalTime;

public record HorarioRequest(
        @Schema(example = "MONDAY", description = "MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY ou SUNDAY")
        @NotNull(message = "O dia da semana é obrigatório")
        DayOfWeek diaSemana,

        @Schema(type = "string", example = "19:00")
        @JsonFormat(pattern = "HH:mm")
        @NotNull(message = "A hora de início é obrigatória")
        LocalTime horaInicio,

        @Schema(type = "string", example = "20:40")
        @JsonFormat(pattern = "HH:mm")
        @NotNull(message = "A hora de fim é obrigatória")
        LocalTime horaFim,

        @NotNull(message = "O professor é obrigatório") Long professorId,
        @NotNull(message = "A disciplina é obrigatória") Long disciplinaId,
        @NotNull(message = "A turma é obrigatória") Long turmaId) {
}
