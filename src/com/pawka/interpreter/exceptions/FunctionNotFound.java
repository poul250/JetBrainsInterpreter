package com.pawka.interpreter.exceptions;

public class FunctionNotFound extends RuntimeException {
    public FunctionNotFound(String name, int line) {
        super("FUNCTION NOT FOUND " + name + ":" + line);
    }
}
