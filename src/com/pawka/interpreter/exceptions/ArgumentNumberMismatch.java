package com.pawka.interpreter.exceptions;

public class ArgumentNumberMismatch extends RuntimeException {
    public ArgumentNumberMismatch(String name, int line) {
        super("ARGUMENT NUMBER MISMATCH " + name + ":" + line);
    }
}
