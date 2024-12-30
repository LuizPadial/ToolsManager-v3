package com.Bio_Controle_Estoque.domain.exceptions;

public class DuplicatedTupleException extends RuntimeException{
    public DuplicatedTupleException(String message) {
        super(message);
    }
}
