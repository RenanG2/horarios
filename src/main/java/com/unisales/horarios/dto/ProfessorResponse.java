package com.unisales.horarios.dto;

import com.unisales.horarios.model.Professor;

public record ProfessorResponse(Long id, String nome, String email) {
    public static ProfessorResponse from(Professor p) {
        return new ProfessorResponse(p.getId(), p.getNome(), p.getEmail());
    }
}
