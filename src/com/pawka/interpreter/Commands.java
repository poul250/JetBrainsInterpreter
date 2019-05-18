package com.pawka.interpreter;

import java.util.Stack;
import java.util.Vector;

public class Commands extends Vector<Commands.Performer> {
    public static interface Performer {
        public void perform(Context context, Stack<Integer> stack);
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
        public void perform(Context context, Stack<Integer> stack) {
            stack.push(value);
        }
    }

    public static class Idle implements Performer {
        @Override
        public void perform(Context context, Stack<Integer> stack) {
        }
    }

    public static class Add implements Performer {
        @Override
        public void perform(Context context, Stack<Integer> stack) {
            int op2 = stack.pop();
            int op1 = stack.pop();
            stack.push(op1 + op2);
        }
    }

    public static class Sub implements Performer {
        @Override
        public void perform(Context context, Stack<Integer> stack) {
            int op2 = stack.pop();
            int op1 = stack.pop();
            stack.push(op1 - op2);
        }
    }

    public static class Mul implements Performer {
        @Override
        public void perform(Context context, Stack<Integer> stack) {
            int op2 = stack.pop();
            int op1 = stack.pop();
            stack.push(op1 * op2);
        }
    }

    public static class Div implements Performer {
        @Override
        public void perform(Context context, Stack<Integer> stack) {
            int op2 = stack.pop();
            int op1 = stack.pop();
            stack.push(op1 / op2);
        }
    }

    public static class Mod implements Performer {
        @Override
        public void perform(Context context, Stack<Integer> stack) {
            int op2 = stack.pop();
            int op1 = stack.pop();
            stack.push(op1 % op2);
        }
    }

    public static class Less implements Performer {
        @Override
        public void perform(Context context, Stack<Integer> stack) {
            int op2 = stack.pop();
            int op1 = stack.pop();
            stack.push((op1 < op2) ? 1 : 0);
        }
    }

    public static class Greater implements Performer {
        @Override
        public void perform(Context context, Stack<Integer> stack) {
            int op2 = stack.pop();
            int op1 = stack.pop();
            stack.push((op1 > op2) ? 1 : 0);
        }
    }

    public static class Equals implements Performer {
        @Override
        public void perform(Context context, Stack<Integer> stack) {
            int op2 = stack.pop();
            int op1 = stack.pop();
            stack.push((op1 == op2) ? 1 : 0);
        }
    }

    public static class IfFalseGoTo implements Performer {
        private int address;
        IfFalseGoTo(int address) {
            this.address = address;
        }
        IfFalseGoTo() {
            this.address = 0;
        }

        @Override
        public void perform(Context context, Stack<Integer> stack) {
            if (stack.pop() == 0) {
                context.address = address;
            }
        }

        public void setAddress(int address) {
            this.address = address;
        }

        public long getAddress() {
            return address;
        }
    }

    public static class GoTo implements Performer {
        private int address;

        public GoTo(int address) {
            this.address = address;
        }

        public GoTo() {
            this.address = 0;
        }

        @Override
        public void perform(Context context, Stack<Integer> stack) {
            context.address = address;
        }

        public void setAddress(int address) {
            this.address = address;
        }

        public long getAddress() {
            return address;
        }
    }
}
