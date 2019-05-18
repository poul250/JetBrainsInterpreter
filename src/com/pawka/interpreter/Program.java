package com.pawka.interpreter;

import java.util.LinkedList;
import java.util.Stack;

public class Program extends LinkedList<Program.Performer> {
    public static interface Performer {
        public void perform(Stack<Integer> stack);
    }

    public Program() {

    }

    public static class Push implements Performer {
        private int value;
        public Push() {
            this(0);
        }
        public Push(int value) {
            this.value = value;
        }

        @Override
        public void perform(Stack<Integer> stack) {
            stack.push(value);
        }
    }

    public static class Add implements Performer {
        @Override
        public void perform(Stack<Integer> stack) {
            int op2 = stack.pop();
            int op1 = stack.pop();
            stack.push(op1 + op2);
        }
    }

    public static class Sub implements Performer {
        @Override
        public void perform(Stack<Integer> stack) {
            int op2 = stack.pop();
            int op1 = stack.pop();
            stack.push(op1 - op2);
        }
    }

    public static class Mul implements Performer {
        @Override
        public void perform(Stack<Integer> stack) {
            int op2 = stack.pop();
            int op1 = stack.pop();
            stack.push(op1 * op2);
        }
    }

    public static class Div implements Performer {
        @Override
        public void perform(Stack<Integer> stack) {
            int op2 = stack.pop();
            int op1 = stack.pop();
            stack.push(op1 / op2);
        }
    }

    public static class Mod implements Performer {
        @Override
        public void perform(Stack<Integer> stack) {
            int op2 = stack.pop();
            int op1 = stack.pop();
            stack.push(op1 % op2);
        }
    }

    int run() {
        Stack<Integer> stack = new Stack<>();
        for (Performer performer : this) {
            performer.perform(stack);
        }
        return stack.peek();
    }
}
