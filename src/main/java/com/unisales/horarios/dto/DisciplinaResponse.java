package com.unisales.horarios.dto;

import com.unisales.horarios.model.Disciplina;

public record DisciplinaResponse(Long id, String nome, Integer cargaHoraria) {
    public static DisciplinaResponse from(Disciplina d) {
        return new DisciplinaResponse(d.getId(), d.getNome(), d.getCargaHoraria());
    }
}
