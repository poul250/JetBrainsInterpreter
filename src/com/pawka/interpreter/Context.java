package com.pawka.interpreter;

import java.util.ArrayList;
import java.util.HashMap;

public class Context {
    public ArrayList<String> argsList;
    public HashMap<String, Integer> variables;
    public static HashMap<String, Commands> functions = new HashMap<>();

    public Commands commands;
    public int address;

    public Context() {
        this(new ArrayList<>());
    }

    public Context(ArrayList<String> argsList) {
        this.argsList = argsList;
        variables = new HashMap<>();
        address = 0;
    }

    public static void addFunction(String name, Commands commands) {
        functions.put(name, commands);
    }
}
