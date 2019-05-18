package com.pawka.interpreter;

import java.util.Stack;

public class Context {
    public Stack<Integer> stack;
    public int address;

    public Context() {
        stack = new Stack<>();
        refresh();
    }

    public void refresh() {
        address = 0;
        stack.clear();
    }
}
