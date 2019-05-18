package com.pawka.interpreter;

import java.util.ArrayList;

public class Function {
    public Commands commands;
    public ArrayList<String> params;
    public int line;

    public Function (ArrayList<String> params, Commands commands, int line) {
        this.params = params;
        this.commands = commands;
        this.line = line;
    }
}
