package com.pawka.interpreter;

import java.util.ArrayList;

public class Function {
    public Commands commands;
    public ArrayList<String> params;

    public Function (ArrayList<String> params, Commands commands) {
        this.params = params;
        this.commands = commands;
    }
}
