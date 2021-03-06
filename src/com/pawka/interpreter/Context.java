package com.pawka.interpreter;

import java.util.HashMap;

public class Context {
    public static HashMap<String, Function> functions = new HashMap<>();

    public HashMap<String, Integer> variables;
    public Commands commands;
    public int address;
    public int line;

    public Context() {
        variables = new HashMap<>();
        address = 0;
    }

    public static void clear() {
        functions.clear();
    }

    public static void addFunction(String name, Function function) {
        functions.put(name, function);
    }
}
