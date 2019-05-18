package com.pawka.interpreter;

import com.pawka.interpreter.exceptions.InterpreterException;

import java.io.*;

public class Main {
    public static void main(String[] args) {
        BufferedReader reader = null;
        if (args.length > 0) {
            try {
                reader = new BufferedReader(new FileReader(args[0]));
            } catch (FileNotFoundException e) {
                System.out.println("Can't open file " + args[0]);
            }
        } else {
            reader = new BufferedReader(new InputStreamReader(System.in));
        }

        Interpreter interpreter = new Interpreter(reader);
        interpreter.interpret();
    }
}
