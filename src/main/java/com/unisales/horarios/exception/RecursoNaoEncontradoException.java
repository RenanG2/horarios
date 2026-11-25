package com.unisales.horarios.exception;

public class RecursoNaoEncontradoException extends RuntimeException {
    public RecursoNaoEncontradoException(String recurso, Long id) {
        super(recurso + " não encontrado(a) com id " + id);
    }
}
