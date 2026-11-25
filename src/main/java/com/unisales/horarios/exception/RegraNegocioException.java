package com.unisales.horarios.exception;

import org.springframework.http.HttpStatus;

/** Violação de regra de negócio (conflito de horário, e-mail duplicado, etc.). */
public class RegraNegocioException extends RuntimeException {
    private final HttpStatus status;

    public RegraNegocioException(String mensagem, HttpStatus status) {
        super(mensagem);
        this.status = status;
    }

    public HttpStatus getStatus() { return status; }
}
