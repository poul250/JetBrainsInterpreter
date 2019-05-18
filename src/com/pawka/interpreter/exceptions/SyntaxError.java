package com.pawka.interpreter.exceptions;

public class SyntaxError extends RuntimeException {
    public SyntaxError() {
        super("SYNTAX_ERROR");
    }
}
