package com.pawka.interpreter;


import com.pawka.interpreter.exceptions.InterpreterException;

import java.io.BufferedReader;
import java.io.IOException;

public class Interpreter {
    private Program program;
    private Lexer lexer;
    private Lexer.Lex currentLexeme;

    public Interpreter(BufferedReader reader) {
        lexer = new Lexer(reader);
        program = new Program();
    }

    private Lexer.Lex moveNext() throws IOException {
        return currentLexeme = lexer.next();
    }

    /**
     * "-"<number>|<number>
     */
    void constExpression() throws InterpreterException, IOException {
        int sign = 1;
        if (currentLexeme instanceof Lexer.LexMinus) {
            sign = -1;
            moveNext();
        }
        if (currentLexeme instanceof Lexer.LexNumber) {
            program.add(new Program.Push(sign * ((Lexer.LexNumber) currentLexeme).number));
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
        if (!(currentLexeme instanceof Lexer.LexParenthesisOpen)) {
            throw new InterpreterException("SYNTAX ERROR");
        }

        // First expression
        moveNext();
        expression();

        // Get operation
        Program.Performer performer = null;
        if (currentLexeme instanceof Lexer.LexPlus) {
            performer = new Program.Add();
        } else if (currentLexeme instanceof Lexer.LexMinus) {
            performer = new Program.Sub();
        } else if (currentLexeme instanceof Lexer.LexMul) {
            performer = new Program.Mul();
        } else if (currentLexeme instanceof Lexer.LexDiv) {
            performer = new Program.Div();
        } else if (currentLexeme instanceof Lexer.LexMod) {
            performer = new Program.Mod();
        } else if (currentLexeme instanceof Lexer.LexLess) {
            performer = new Program.Less();
        } else if (currentLexeme instanceof Lexer.LexGreater) {
            performer = new Program.Greater();
        } else if (currentLexeme instanceof Lexer.LexEquals) {
            performer = new Program.Equals();
        }

        // Second expression
        moveNext();
        expression();

        // Push operation for POLIZ
        program.add(performer);

        // Check closing bracket
        if (!(currentLexeme instanceof Lexer.LexParenthesisClose)) {
            throw new InterpreterException("SYNTAX ERROR");
        }
        moveNext();
    }

    /**
     * "["<expression>"]?{"<expression>"}:{"<expression>"}"
     *
     * [expression]?
     * ifFalseGoTo-----------------
     * {expression}               |
     * goTo-----------------------+----
     *                            |   |
     * {expression}<---------------   |
     * <-------------------------------
     */
    void ifExpression() throws IOException, InterpreterException {
        // "["
        if (!(currentLexeme instanceof Lexer.LexSquareOpen)) {
            throw new InterpreterException("SYNTAX ERROR");
        }

        // <expression>
        moveNext();
        expression();

        // goto after if statement
        Program.IfFalseGoTo ifFalseGoTo = new Program.IfFalseGoTo();
        program.add(ifFalseGoTo);

        // "]"
        if (!(currentLexeme instanceof Lexer.LexSquareClose)) {
            throw new InterpreterException("SYNTAX ERROR");
        }

        // "?"
        if (!(moveNext() instanceof Lexer.LexQuestionMark)) {
            throw new InterpreterException("SYNTAX ERROR");
        }

        // "{"
        if (!(moveNext() instanceof Lexer.LexBraceOpen)) {
            throw new InterpreterException("SYNTAX ERROR");
        }

        // <expression>
        moveNext();
        expression();

        // jump from here to end of false block
        Program.GoTo goTo = new Program.GoTo();
        program.add(goTo);

        // jump here if expression is false
        ifFalseGoTo.setAddress(program.size());
        // "}"
        if (!(currentLexeme instanceof Lexer.LexBraceClose)) {
            throw new InterpreterException("SYNTAX ERROR");
        }

        // ":"
        if (!(moveNext() instanceof Lexer.LexColon)) {
            throw new InterpreterException("SYNTAX ERROR");
        }

        // "{"
        if (!(moveNext() instanceof Lexer.LexBraceOpen)) {
            throw new InterpreterException("SYNTAX ERROR");
        }

        // <expression>
        moveNext();
        expression();

        // jump here after true block
        goTo.setAddress(program.size());

        // "}"
        if (!(currentLexeme instanceof Lexer.LexBraceClose)) {
            throw new InterpreterException("SYNTAX ERROR");
        }

        moveNext();
    }

    /**
     * <identifier>|<constant-expression>|<binary-expression>|<if-expression>|<call-expression>
     */
    void expression() throws IOException, InterpreterException {
        if (currentLexeme instanceof Lexer.LexIdentifier) {
            System.err.println("Unrealized feature expresion:indentifer");
            System.exit(-1);
        } else if (currentLexeme instanceof Lexer.LexParenthesisOpen) {
            binaryOperation();
        } else if ((currentLexeme instanceof Lexer.LexMinus) || (currentLexeme instanceof Lexer.LexNumber)) {
            constExpression();
        } else if (currentLexeme instanceof Lexer.LexSquareOpen) {
            ifExpression();
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
