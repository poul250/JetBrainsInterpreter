package com.pawka.interpreter;


import com.pawka.interpreter.exceptions.InterpreterException;

import java.io.BufferedReader;
import java.io.IOException;

public class Interpreter {
    private Program program;
    private Lexer lexer;
    private Lexer.Lex current;

    public Interpreter(BufferedReader reader) {
        lexer = new Lexer(reader);
        program = new Program();
    }

    private Lexer.Lex moveNext() throws IOException {
        return current = lexer.next();
    }

    /**
     * "-"<number>|<number>
     */
    void constExpression() throws InterpreterException, IOException {
        int sign = 1;
        if (current instanceof Lexer.LexMinus) {
            sign = -1;
            moveNext();
        }
        if (current instanceof Lexer.LexNumber) {
            program.add(new Program.Push(sign * ((Lexer.LexNumber)current).number));
        } else {
            throw new InterpreterException("SYNTAX ERROR");
        }
        moveNext();
    }

    /**
     * "("<expression><operation><expression>")"
     */
    void binaryOperation() throws IOException, InterpreterException {
        // Check for opening bracket
        if (!(current instanceof Lexer.LexParenthesisOpen)) {
            throw new InterpreterException("SYNTAX ERROR");
        }

        // First expression
        moveNext();
        expression();

        // Get operation
        Program.Performer performer = null;
        if (current instanceof Lexer.LexPlus) {
            performer = new Program.Add();
        } else if (current instanceof Lexer.LexMinus) {
            performer = new Program.Sub();
        } else if (current instanceof Lexer.LexMul) {
            performer = new Program.Mul();
        } else if (current instanceof Lexer.LexDiv) {
            performer = new Program.Div();
        } else if (current instanceof Lexer.LexMod) {
            performer = new Program.Mod();
        }

        // Second expression
        moveNext();
        expression();

        // Push operation for POLIZ
        program.add(performer);

        // Check closing bracket
        if (!(current instanceof Lexer.LexParenthesisClose)) {
            throw new InterpreterException("SYNTAX_ERROR");
        }
        moveNext();
    }

    /**
     * <identifier>|<constant-expression>|<binary-expression>|<if-expression>|<call-expression>
     */
    void expression() throws IOException, InterpreterException {
        if (current instanceof Lexer.LexIdentifier) {
            System.err.println("Unrealized feature expresion:indentifer");
            System.exit(-1);
        } else if (current instanceof Lexer.LexParenthesisOpen) {
            binaryOperation();
        } else if ((current instanceof Lexer.LexMinus) || (current instanceof Lexer.LexNumber)) {
            constExpression();
        } else {
            throw new InterpreterException("SYNTAX_ERROR");
        }
    }

    /**
     * <function-definition-list><expression>
     */
    void interpret() throws IOException, InterpreterException {
        moveNext();
        expression();

        System.out.println(program.run());
    }
}
