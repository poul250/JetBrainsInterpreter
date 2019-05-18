package com.pawka.interpreter;

import com.pawka.interpreter.exceptions.InterpreterException;
import com.pawka.interpreter.exceptions.LexerException;

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
        try {
            interpreter.interpret();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (LexerException e) {
            System.out.println(e.getMessage());
        } catch (InterpreterException e) {
            System.out.println(e.getMessage());
        }
    }
}
