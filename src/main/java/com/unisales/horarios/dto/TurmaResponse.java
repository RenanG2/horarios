package com.unisales.horarios.dto;

import com.unisales.horarios.model.Turma;

public record TurmaResponse(Long id, String nome, String semestre) {
    public static TurmaResponse from(Turma t) {
        return new TurmaResponse(t.getId(), t.getNome(), t.getSemestre());
    }
}
