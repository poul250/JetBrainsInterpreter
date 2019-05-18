package com.pawka.interpreter.exceptions;

public class ParameterNotFound extends RuntimeException {
    public ParameterNotFound(String name, int line) {
        super("PARAMETER NOT FOUND " + name + ":" + line);
    }
}
