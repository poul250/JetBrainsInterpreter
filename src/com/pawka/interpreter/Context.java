package com.pawka.interpreter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class Context {
    public Stack<Integer> stack;
    public ArrayList<String> argsList;
    public HashMap<String, Integer> variables;
    public static HashMap<String, Program> functions = new HashMap<>();
    public int address;

    public Context() {
        this(new ArrayList<>());
    }

    public Context(ArrayList<String> argsList) {
        this.argsList = argsList;
        stack = new Stack<>();
        variables = new HashMap<>();
        refresh();
    }

    public void refresh() {
        address = 0;
        stack.clear();
    }

    public static void addFunction(String name, Program program) {
        functions.put(name, program);
    }
}
