package com.pawka.interpreter;

import com.pawka.interpreter.exceptions.LexerException;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;

public class Lexer {

    private BufferedReader reader;
    private StringBuilder stringBuilder;
    private static HashMap<Integer, Class<? extends Lex>> map;

    private int line = 0;
    private boolean readNewChar = true;
    private int ch;

    private enum State {
        STATE_NONE,
        STATE_READ_NUM,
        STATE_READ_ID
    }

    private State state;

    /**
     * Lexeme class
     */
    public static class Lex {
        public Lex() {
        }
    }

    /**
     * create lexeme here, and then return
     */
    private Lex lex;

    public static class LexNumber extends Lex {
        LexNumber(int number) {
            this.number = number;
        }
        public int number;
    }

    public static class LexIdentifier extends Lex {
        LexIdentifier(String identifier) {
            this.identifier = identifier;
        }
        public String identifier;
    }

    public static class LexPlus extends Lex {
        public LexPlus() {
        }
    }

    public static class LexMinus extends Lex {
        public LexMinus() {
        }
    }

    public static class LexMul extends Lex {
        public LexMul() {
        }
    }

    public static class LexDiv extends Lex {
        public LexDiv() {
        }
    }

    public static class LexMod extends Lex {
        public LexMod() {
        }
    }

    public static class LexGreater extends Lex {
        public LexGreater() {
        }
    }

    public static class LexLess extends Lex {
        public LexLess() {
        }
    }

    public static class LexEquals extends Lex {
        public LexEquals() {
        }
    }

    public static class LexParenthesisOpen extends Lex {
        public LexParenthesisOpen() {
        }
    }

    public static class LexParenthesisClose extends Lex {
        public LexParenthesisClose() {
        }
    }

    public static class LexSquareOpen extends Lex {
        public LexSquareOpen() {
        }
    }

    public static class LexSquareClose extends Lex {
        public LexSquareClose() {
        }
    }

    public static class LexBraceOpen extends Lex {
        public LexBraceOpen() {
        }
    }

    public static class LexBraceClose extends Lex {
        public LexBraceClose() {
        }
    }

    public static class LexQuestionMark extends Lex {
        public LexQuestionMark() {
        }
    }

    public static class LexColon extends Lex {
        public LexColon() {
        }
    }

    public static class LexComma extends Lex {
        public LexComma() {
        }
    }

    public static class LexEOL extends Lex {
        public LexEOL() {
        }
    }

    public static class LexEOF extends Lex {
        public LexEOF() {
        }
    }

    public Lexer(BufferedReader reader) {
        if (map == null) {
            map = new HashMap<Integer, Class<? extends Lex>>();
            map.put((int)'+', LexPlus.class);
            map.put((int)'-', LexMinus.class);
            map.put((int)'*', LexMul.class);
            map.put((int)'/', LexDiv.class);
            map.put((int)'%', LexMod.class);
            map.put((int)'>', LexGreater.class);
            map.put((int)'<', LexLess.class);
            map.put((int)'=', LexEquals.class);
            map.put((int)'(', LexParenthesisOpen.class);
            map.put((int)')', LexParenthesisClose.class);
            map.put((int)'[', LexSquareOpen.class);
            map.put((int)']', LexSquareClose.class);
            map.put((int)'{', LexBraceOpen.class);
            map.put((int)'}', LexBraceClose.class);
            map.put((int)'?', LexQuestionMark.class);
            map.put((int)':', LexColon.class);
            map.put((int)',', LexComma.class);
            map.put((int)'\n', LexEOL.class);
        }
        this.reader = reader;
        stringBuilder = new StringBuilder();
        state = State.STATE_NONE;
        line = 1;
    }

    private boolean stateNone(int ch) throws LexerException{
        if (ch == -1) {
            lex = new LexEOF();
        } else if (map.containsKey(ch)) {
            try {
                lex = (Lex)(map.get(ch).newInstance());
            } catch (InstantiationException e) {
                // impossible
                e.printStackTrace();
                System.exit(-1);
            } catch (IllegalAccessException e) {
                // impossible
                e.printStackTrace();
                System.exit(-1);
            }
        } else if (Character.isDigit(ch)) {
            stringBuilder.append((char)ch);
            state = State.STATE_READ_NUM;
        } else if (Character.isLetter(ch) || ch == '_') {
            stringBuilder.append((char)ch);
            state = State.STATE_READ_ID;
        } else {
            throw new LexerException("SYNTAX_ERROR");
        }

        return true;
    }

    private boolean stateReadNum(int ch) {
        if (Character.isDigit(ch)) {
            stringBuilder.append(ch);
        } else {
            lex = new LexNumber(Integer.parseInt(stringBuilder.toString()));
            stringBuilder.setLength(0);
            state = State.STATE_NONE;
            return false;
        }

        return true;
    }

    private boolean stateReadId(int ch) throws LexerException {
        if (Character.isLetter(ch) || ch == '_') {
            stringBuilder.append(ch);
        } else {
            lex = new LexIdentifier(stringBuilder.toString());
            stringBuilder.setLength(0);
            state = State.STATE_NONE;
            return false;
        }
        return true;
    }

    public Lex next() throws IOException, LexerException {
        lex = null;

        while (lex == null) {
            if (readNewChar) {
                ch = reader.read();
                if (ch == '\n') {
                    ++line;
                }
            }

            switch (state) {
                case STATE_NONE:
                    readNewChar = stateNone(ch);
                    break;
                case STATE_READ_NUM:
                    readNewChar = stateReadNum(ch);
                    break;
                case STATE_READ_ID:
                    readNewChar = stateReadId(ch);
                    break;
                default:
                    System.err.println("Unrealized state");
                    System.exit(-1);
                    break;
            }
        }
        return lex;
    }

    public int getLine() {
        return line;
    }
}
