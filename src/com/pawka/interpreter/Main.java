package com.pawka.interpreter;

import com.pawka.interpreter.exceptions.ArgumentNumberMismatch;
import com.pawka.interpreter.exceptions.FunctionNotFound;
import com.pawka.interpreter.exceptions.ParameterNotFound;
import com.pawka.interpreter.exceptions.SyntaxError;
import jdk.nashorn.internal.runtime.regexp.joni.Syntax;

import java.io.*;

public class Main {
    public static void main(String[] args) {
        BufferedReader reader = null;
        if (args.length > 0) {
            try {
                reader = new BufferedReader(new FileReader(args[0]));
            } catch (FileNotFoundException e) {
                System.out.println("Can't open file " + args[0]);
                return;
            }
        } else {
            reader = new BufferedReader(new InputStreamReader(System.in));
        }

        Interpreter interpreter = new Interpreter(reader);
        try {
            interpreter.interpret();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SyntaxError e) {
            System.out.println(e.getMessage());
        } catch (ArgumentNumberMismatch e) {
            System.out.println(e.getMessage());
        } catch (FunctionNotFound e) {
            System.out.println(e.getMessage());
        } catch (ParameterNotFound e) {
            System.out.println(e.getMessage());
        } catch (RuntimeException e) {
            System.out.println(e);
        }
    }
}
