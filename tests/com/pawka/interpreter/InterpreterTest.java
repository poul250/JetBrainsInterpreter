package com.pawka.interpreter;

import com.pawka.interpreter.exceptions.ArgumentNumberMismatch;
import com.pawka.interpreter.exceptions.FunctionNotFound;
import com.pawka.interpreter.exceptions.ParameterNotFound;
import com.pawka.interpreter.exceptions.SyntaxError;
import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
public class InterpreterTest {
    @Test
    public void test0() {
        String program = "(2+2)";
        BufferedReader reader = new BufferedReader(new StringReader(program));
        Interpreter interpreter = new Interpreter(reader);

        int result = 0;
        try {
            result = interpreter.interpret();
        } catch (IOException e) {
            Assert.fail("Unexpected IOExeption");
        } catch (RuntimeException e) {
            Assert.fail("Unexpected: " + e.getMessage());
        }
        Assert.assertEquals(result, 4);
    }

    @Test
    public void test1() {
        String program = "(2+((3*4)/5))";
        BufferedReader reader = new BufferedReader(new StringReader(program));
        Interpreter interpreter = new Interpreter(reader);

        int result = 0;
        try {
            result = interpreter.interpret();
        } catch (IOException e) {
            Assert.fail("Unexpected IOExeption");
        } catch (RuntimeException e) {
            Assert.fail("Unexpected: " + e.getMessage());
        }
        Assert.assertEquals(result, 4);
    }

    @Test
    public void test2() {
        String program = "[((10+20)>(20+10))]?{1}:{0}";
        BufferedReader reader = new BufferedReader(new StringReader(program));
        Interpreter interpreter = new Interpreter(reader);

        int result = 0;
        try {
            result = interpreter.interpret();
        } catch (IOException e) {
            Assert.fail("Unexpected IOExeption");
        } catch (RuntimeException e) {
            Assert.fail("Unexpected: " + e.getMessage());
        }
        Assert.assertEquals(result, 0);
    }


    @Test
    public void test3() {
        String program = "g(x)={(f(x)+f((x/2)))}\n" +
                         "f(x)={[(x>1)]?{(f((x-1))+f((x-2)))}:{x}}\n" +
                         "g(10)";
        BufferedReader reader = new BufferedReader(new StringReader(program));
        Interpreter interpreter = new Interpreter(reader);

        int result = 0;
        try {
            result = interpreter.interpret();
        } catch (IOException e) {
            Assert.fail("Unexpected IOExeption");
        } catch (RuntimeException e) {
            Assert.fail("Unexpected: " + e.getMessage());
        }
        Assert.assertEquals(result, 60);
    }

    @Test
    public void test4() {
        String program = "1 + 2 + 3 + 4 + 5";
        BufferedReader reader = new BufferedReader(new StringReader(program));
        Interpreter interpreter = new Interpreter(reader);

        try {
            interpreter.interpret();
        } catch (IOException e) {
            Assert.fail("Unexpected IOExeption");
        } catch (SyntaxError e) {
            Assert.assertEquals("SYNTAX ERROR", e.getMessage());
            return;
        } catch (RuntimeException e) {
            Assert.fail("Unexpected: " + e.getMessage());
        }
        Assert.fail("Expected Syntax Error");
    }

    @Test
    public void test5() {
        String program = "f(x)={y}\n" +
                         "f(10)";
        BufferedReader reader = new BufferedReader(new StringReader(program));
        Interpreter interpreter = new Interpreter(reader);

        try {
            interpreter.interpret();
        } catch (IOException e) {
            Assert.fail("Unexpected IOExeption");
        } catch (ParameterNotFound e) {
            Assert.assertEquals("PARAMETER NOT FOUND y:1", e.getMessage());
            return;
        } catch (RuntimeException e) {
            Assert.fail("Unexpected: " + e.getMessage());
        }
        Assert.fail("Expected parameter not found");
    }

    @Test
    public void test6() {
        String program = "g(x)={(x+1)}\n" +
                         "g(10,20)";
        BufferedReader reader = new BufferedReader(new StringReader(program));
        Interpreter interpreter = new Interpreter(reader);

        try {
            interpreter.interpret();
        } catch (IOException e) {
            Assert.fail("Unexpected IOExeption");
        } catch (ArgumentNumberMismatch e) {
            Assert.assertEquals("ARGUMENT NUMBER MISMATCH g:2", e.getMessage());
            return;
        } catch (RuntimeException e) {
            Assert.fail("Unexpected: " + e.getMessage());
        }
        Assert.fail("Expected parameter not found");
    }

    @Test
    public void test7() {
        String program = "g(x)={f(x)}\n" +
                         "g(10)";
        BufferedReader reader = new BufferedReader(new StringReader(program));
        Interpreter interpreter = new Interpreter(reader);

        try {
            interpreter.interpret();
        } catch (IOException e) {
            Assert.fail("Unexpected IOExeption");
        } catch (FunctionNotFound e) {
            Assert.assertEquals("FUNCTION NOT FOUND f:1", e.getMessage());
            return;
        } catch (RuntimeException e) {
            Assert.fail("Unexpected: " + e.getMessage());
        }
        Assert.fail("Expected parameter not found");
    }

    @Test
    public void factorial() {
        String program = "f(x)={[(x>1)]?{(x*f((x-1)))}:{1}}\n" +
                         "f(10)";
        BufferedReader reader = new BufferedReader(new StringReader(program));
        Interpreter interpreter = new Interpreter(reader);

        int result = 0;
        try {
            result = interpreter.interpret();
        } catch (IOException e) {
            Assert.fail("Unexpected IOExeption");
        } catch (RuntimeException e) {
            Assert.fail("Unexpected: " + e.getMessage());
        }
        Assert.assertEquals(result, 3628800);
    }

    @Test
    public void min() {
        String program = "min(x,y)={[(x<y)]?{x}:{y}}\n" +
                         "min(3,5)";
        BufferedReader reader = new BufferedReader(new StringReader(program));
        Interpreter interpreter = new Interpreter(reader);

        int result = 0;
        try {
            result = interpreter.interpret();
        } catch (IOException e) {
            Assert.fail("Unexpected IOExeption");
        } catch (RuntimeException e) {
            Assert.fail("Unexpected: " + e.getMessage());
        }
        Assert.assertEquals(result, 3);
    }

    @Test
    public void mean() {
        String program = "min(x,y)={[(x<y)]?{x}:{y}}\n" +
                         "minthree(x,y,z)={min(min(x,y),z)}\n" +
                         "max(x,y)={[(x>y)]?{x}:{y}}\n" +
                         "maxthree(x,y,z)={max(max(x,y),z)}\n" +
                         "sum(a,b,c)={((a+b)+c)}\n"     +
                         "mean(a,b,c)={((sum(a,b,c)-minthree(a,b,c))-maxthree(a,b,c))}\n"+
                         "mean(-1,5,10)";
        BufferedReader reader = new BufferedReader(new StringReader(program));
        Interpreter interpreter = new Interpreter(reader);

        int result = 0;
        try {
            result = interpreter.interpret();
        } catch (IOException e) {
            Assert.fail("Unexpected IOExeption");
        } catch (RuntimeException e) {
            Assert.fail("Unexpected: " + e.getMessage());
        }
        Assert.assertEquals(result, 5);
    }
}
