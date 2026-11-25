package com.unisales.horarios.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.unisales.horarios.model.Horario;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.DayOfWeek;
import java.time.LocalTime;

public record HorarioResponse(
        Long id,
        DayOfWeek diaSemana,
        @Schema(type = "string", example = "19:00") @JsonFormat(pattern = "HH:mm") LocalTime horaInicio,
        @Schema(type = "string", example = "20:40") @JsonFormat(pattern = "HH:mm") LocalTime horaFim,
        Long professorId, String professorNome,
        Long disciplinaId, String disciplinaNome,
        Long turmaId, String turmaNome) {

    public static HorarioResponse from(Horario h) {
        return new HorarioResponse(
                h.getId(), h.getDiaSemana(), h.getHoraInicio(), h.getHoraFim(),
                h.getProfessor().getId(), h.getProfessor().getNome(),
                h.getDisciplina().getId(), h.getDisciplina().getNome(),
                h.getTurma().getId(), h.getTurma().getNome());
    }
}
