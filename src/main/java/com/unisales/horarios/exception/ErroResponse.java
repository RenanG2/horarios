package com.unisales.horarios.exception;

import java.time.LocalDateTime;
import java.util.List;

/** Formato padrão de resposta de erro da API. */
public record ErroResponse(LocalDateTime timestamp, int status, String erro, String mensagem, List<String> detalhes) {
    public static ErroResponse of(int status, String erro, String mensagem, List<String> detalhes) {
        return new ErroResponse(LocalDateTime.now(), status, erro, mensagem, detalhes);
    }
}
