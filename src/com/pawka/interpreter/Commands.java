package com.pawka.interpreter;

import java.util.Stack;
import java.util.Vector;

public class Commands extends Vector<Commands.Performer> {
    public static interface Performer {
        public void perform(Stack<Context> contextStack, Stack<Integer> programStack);
    }

    public static class Idle implements Performer {
        @Override
        public void perform(Stack<Context> contextStack, Stack<Integer> programStack) {
        }
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
        public void perform(Stack<Context> contextStack, Stack<Integer> programStack) {
            programStack.push(value);
        }
    }

    public static class PushVar implements Performer {
        private String name;
        public PushVar(String name) {
            this.name = name;
        }

        @Override
        public void perform(Stack<Context> contextStack, Stack<Integer> programStack) {
            programStack.push(contextStack.peek().variables.get(name));
        }
    }



    public static class Add implements Performer {
        @Override
        public void perform(Stack<Context> contextStack, Stack<Integer> programStack) {
            int op2 = programStack.pop();
            int op1 = programStack.pop();
            programStack.push(op1 + op2);
        }
    }

    public static class Sub implements Performer {
        @Override
        public void perform(Stack<Context> contextStack, Stack<Integer> programStack) {
            int op2 = programStack.pop();
            int op1 = programStack.pop();
            programStack.push(op1 - op2);
        }
    }

    public static class Mul implements Performer {
        @Override
        public void perform(Stack<Context> contextStack, Stack<Integer> programStack) {
            int op2 = programStack.pop();
            int op1 = programStack.pop();
            programStack.push(op1 * op2);
        }
    }

    public static class Div implements Performer {
        @Override
        public void perform(Stack<Context> contextStack, Stack<Integer> programStack) {
            int op2 = programStack.pop();
            int op1 = programStack.pop();
            programStack.push(op1 / op2);
        }
    }

    public static class Mod implements Performer {
        @Override
        public void perform(Stack<Context> contextStack, Stack<Integer> programStack) {
            int op2 = programStack.pop();
            int op1 = programStack.pop();
            programStack.push(op1 % op2);
        }
    }

    public static class Less implements Performer {
        @Override
        public void perform(Stack<Context> contextStack, Stack<Integer> programStack) {
            int op2 = programStack.pop();
            int op1 = programStack.pop();
            programStack.push((op1 < op2) ? 1 : 0);
        }
    }

    public static class Greater implements Performer {
        @Override
        public void perform(Stack<Context> contextStack, Stack<Integer> programStack) {
            int op2 = programStack.pop();
            int op1 = programStack.pop();
            programStack.push((op1 > op2) ? 1 : 0);
        }
    }

    public static class Equals implements Performer {
        @Override
        public void perform(Stack<Context> contextStack, Stack<Integer> programStack) {
            int op2 = programStack.pop();
            int op1 = programStack.pop();
            programStack.push((op1 == op2) ? 1 : 0);
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
        public void perform(Stack<Context> contextStack, Stack<Integer> programStack) {
            if (programStack.pop() == 0) {
                contextStack.peek().address = address;
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
        public void perform(Stack<Context> contextStack, Stack<Integer> programStack) {
            contextStack.peek().address = address;
        }

        public void setAddress(int address) {
            this.address = address;
        }

        public long getAddress() {
            return address;
        }
    }

    public static class FunctionCall implements Performer {
        private String name;

        public FunctionCall(String name) {
            this.name = name;
        }

        @Override
        public void perform(Stack<Context> contextStack, Stack<Integer> programStack) {
            Function func = Context.functions.get(name);
            Context context = new Context();
            context.commands = func.commands;
            for (int i = func.params.size() - 1; i >= 0; --i) {
                context.variables.put(func.params.get(i), programStack.pop());
            }
            contextStack.push(context);
        }
    }

    public static class ExitFunction implements Performer {
        @Override
        public void perform(Stack<Context> contextStack, Stack<Integer> programStack) {
            contextStack.pop();
        }
    }
}
