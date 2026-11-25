package com.unisales.horarios.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

public record DisciplinaRequest(
        @Schema(example = "Programação Web")
        @NotBlank(message = "O nome é obrigatório")
        @Size(max = 120, message = "O nome deve ter no máximo 120 caracteres")
        String nome,

        @Schema(example = "80", description = "Carga horária total em horas")
        @NotNull(message = "A carga horária é obrigatória")
        @Min(value = 1, message = "A carga horária deve ser de pelo menos 1 hora")
        @Max(value = 1000, message = "A carga horária deve ser de no máximo 1000 horas")
        Integer cargaHoraria) {
}
