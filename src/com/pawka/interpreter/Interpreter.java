package com.pawka.interpreter;


import com.pawka.interpreter.exceptions.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

public class Interpreter {
    private Program program;
    private Commands commands;
    private ArrayList<String> functionArgs;
    private Lexer lexer;
    private Lexer.Lex currentLexeme;

    private Stack<Lexer.Lex> lexBuffer;

    public Interpreter(BufferedReader reader) {
        lexer = new Lexer(reader);
        program = new Program();
        commands = new Commands();
        functionArgs = new ArrayList<>();
        lexBuffer = new Stack<>();
    }

    private Lexer.Lex moveNext() throws IOException {
        if (lexBuffer.isEmpty()) {
            return currentLexeme = lexer.next();
        }

        return currentLexeme = lexBuffer.pop();
    }

    private void unget(Lexer.Lex lex) {
        lexBuffer.push(currentLexeme);
        currentLexeme = lex;
    }

    /**
     * "-"<number>|<number>
     */
    void constExpression() throws IOException {
        int sign = 1;
        if (currentLexeme instanceof Lexer.LexMinus) {
            sign = -1;
            moveNext();
        }
        if (currentLexeme instanceof Lexer.LexNumber) {
            commands.add(new Commands.Push(sign * ((Lexer.LexNumber) currentLexeme).number));
        } else {
            throw new SyntaxError();
        }
        moveNext();
    }

    /**
     * "("<expression><operation><expression>")"
     */
    void binaryOperation() throws IOException{
        // Check for opening bracket
        if (!(currentLexeme instanceof Lexer.LexParenthesisOpen)) {
            throw new SyntaxError();
        }

        // First expression
        moveNext();
        expression();

        // Get operation
        Commands.Performer performer = null;
        if (currentLexeme instanceof Lexer.LexPlus) {
            performer = new Commands.Add();
        } else if (currentLexeme instanceof Lexer.LexMinus) {
            performer = new Commands.Sub();
        } else if (currentLexeme instanceof Lexer.LexMul) {
            performer = new Commands.Mul();
        } else if (currentLexeme instanceof Lexer.LexDiv) {
            performer = new Commands.Div();
        } else if (currentLexeme instanceof Lexer.LexMod) {
            performer = new Commands.Mod();
        } else if (currentLexeme instanceof Lexer.LexLess) {
            performer = new Commands.Less();
        } else if (currentLexeme instanceof Lexer.LexGreater) {
            performer = new Commands.Greater();
        } else if (currentLexeme instanceof Lexer.LexEquals) {
            performer = new Commands.Equals();
        }

        // Second expression
        moveNext();
        expression();

        // Push operation for POLIZ
        commands.add(performer);

        // Check closing bracket
        if (!(currentLexeme instanceof Lexer.LexParenthesisClose)) {
            throw new SyntaxError();
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
    void ifExpression() throws IOException{
        // "["
        if (!(currentLexeme instanceof Lexer.LexSquareOpen)) {
            throw new SyntaxError();
        }

        // <expression>
        moveNext();
        expression();

        // goto after if statement
        Commands.IfFalseGoTo ifFalseGoTo = new Commands.IfFalseGoTo();
        commands.add(ifFalseGoTo);

        // "]"
        if (!(currentLexeme instanceof Lexer.LexSquareClose)) {
            throw new SyntaxError();
        }

        // "?"
        if (!(moveNext() instanceof Lexer.LexQuestionMark)) {
            throw new SyntaxError();
        }

        // "{"
        if (!(moveNext() instanceof Lexer.LexBraceOpen)) {
            throw new SyntaxError();
        }

        // <expression>
        moveNext();
        expression();

        // jump from here to end of false block
        Commands.GoTo goTo = new Commands.GoTo();
        commands.add(goTo);

        // jump here if expression is false
        ifFalseGoTo.setAddress(commands.size());
        // "}"
        if (!(currentLexeme instanceof Lexer.LexBraceClose)) {
            throw new SyntaxError();
        }

        // ":"
        if (!(moveNext() instanceof Lexer.LexColon)) {
            throw new SyntaxError();
        }

        // "{"
        if (!(moveNext() instanceof Lexer.LexBraceOpen)) {
            throw new SyntaxError();
        }

        // <expression>
        moveNext();
        expression();

        // jump here after true block
        goTo.setAddress(commands.size());

        // "}"
        if (!(currentLexeme instanceof Lexer.LexBraceClose)) {
            throw new SyntaxError();
        }

        moveNext();
    }

    void identifier() throws IOException {
        if (!(currentLexeme instanceof Lexer.LexIdentifier)) {
            throw new SyntaxError();
        }
        String name = ((Lexer.LexIdentifier) currentLexeme).identifier;
        if (!(functionArgs.contains(name))) {
            throw new ParameterNotFound(name, lexer.getLine());
        }
        commands.add(new Commands.PushVar(name));
        moveNext();
    }

    void argumentList(String name) throws IOException{
        Function func = Context.functions.get(name);
        int counter = 1;

        expression();
        while (currentLexeme instanceof Lexer.LexComma) {
            moveNext();
            expression();
            ++counter;
        }

        if (func.params.size() != counter) {
            throw new ArgumentNumberMismatch(name, lexer.getLine());
        }
    }

    /**
     * <identifier> "(" <argument-list> ")"
     */
    void callExpression() throws IOException {
        String name = ((Lexer.LexIdentifier) currentLexeme).identifier;
        if (!(moveNext() instanceof Lexer.LexParenthesisOpen)) {
            throw new SyntaxError();
        }

        if (!Context.functions.containsKey(name)) {
            throw new FunctionNotFound(name, lexer.getLine());
        }

        moveNext();
        argumentList(name);

        commands.add(new Commands.FunctionCall(name));
        if (!(currentLexeme instanceof Lexer.LexParenthesisClose)) {
            throw new SyntaxError();
        }
        moveNext();
    }

    /**
     * <identifier>|<constant-expression>|<binary-expression>|<if-expression>|<call-expression>
     */
    void expression() throws IOException {
        if (currentLexeme instanceof Lexer.LexIdentifier) {
            Lexer.LexIdentifier name = (Lexer.LexIdentifier) currentLexeme;
            if (moveNext() instanceof Lexer.LexParenthesisOpen) {
                unget(name);
                callExpression();
            } else {
                unget(name);
                identifier();
            }
        } else if (currentLexeme instanceof Lexer.LexParenthesisOpen) {
            binaryOperation();
        } else if ((currentLexeme instanceof Lexer.LexMinus) || (currentLexeme instanceof Lexer.LexNumber)) {
            constExpression();
        } else if (currentLexeme instanceof Lexer.LexSquareOpen) {
            ifExpression();
        } else {
            throw new SyntaxError();
        }
    }

    void parameterList() throws IOException{
        if (currentLexeme instanceof Lexer.LexIdentifier) {
            functionArgs.add(((Lexer.LexIdentifier)currentLexeme).identifier);
        }

        while (moveNext() instanceof Lexer.LexComma) {
            if (!(moveNext() instanceof Lexer.LexIdentifier)) {
                throw new SyntaxError();
            }
            functionArgs.add(((Lexer.LexIdentifier)currentLexeme).identifier);
        }
    }

    /**
     * <identifier>"(" <parameter_list> ")" "={" <expression> "}"
     */
    void functionDefinition() throws IOException {
        parameterList();

        if (!(currentLexeme instanceof Lexer.LexParenthesisClose)) {
            throw new SyntaxError();
        }

        if (!(moveNext() instanceof Lexer.LexEquals)) {
            throw new SyntaxError();
        }

        if (!(moveNext() instanceof Lexer.LexBraceOpen)) {
            throw new SyntaxError();
        }

        moveNext();
        expression();
        commands.add(new Commands.ExitFunction());

        if (!(currentLexeme instanceof Lexer.LexBraceClose)) {
            throw new SyntaxError();
        }
        moveNext();
    }

    void functionDefinitionList() throws IOException{
        while (true) {
            if (!(currentLexeme instanceof Lexer.LexIdentifier)) {
                return;
            }

            Lexer.LexIdentifier name = (Lexer.LexIdentifier) currentLexeme;
            if (!(moveNext() instanceof Lexer.LexParenthesisOpen)) {
                throw new SyntaxError();
            }

            if (!(moveNext() instanceof Lexer.LexIdentifier)) {
                unget(new Lexer.LexParenthesisOpen());
                unget(name);
                return;
            }

            functionDefinition();
            if (!(currentLexeme instanceof Lexer.LexEOL)) {
                throw new SyntaxError();
            }
            moveNext();

            // create new function
            Context.addFunction(name.identifier, new Function(functionArgs, commands));
            commands = new Commands();
            functionArgs = new ArrayList<>();
        }
    }

    /**
     * <function-definition-list><expression>
     */
    void interpret() {
        try {
            moveNext();
            functionDefinitionList();
            expression();
            commands.add(new Commands.ExitFunction());
            System.out.println(program.run(commands));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }
}
