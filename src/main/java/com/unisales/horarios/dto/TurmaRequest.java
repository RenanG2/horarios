package com.unisales.horarios.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record TurmaRequest(
        @Schema(example = "ADS 3º Período")
        @NotBlank(message = "O nome é obrigatório")
        @Size(max = 60, message = "O nome deve ter no máximo 60 caracteres")
        String nome,

        @Schema(example = "2026/2", description = "Formato AAAA/S, onde S é 1 ou 2")
        @NotBlank(message = "O semestre é obrigatório")
        @Pattern(regexp = "\\d{4}/[12]", message = "Semestre deve estar no formato AAAA/1 ou AAAA/2")
        String semestre) {
}
