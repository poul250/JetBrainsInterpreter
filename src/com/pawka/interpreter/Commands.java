package com.pawka.interpreter;

import com.pawka.interpreter.exceptions.ArgumentNumberMismatch;
import com.pawka.interpreter.exceptions.FunctionNotFound;

import java.util.Stack;
import java.util.Vector;

public class Commands extends Vector<Commands.Performer> {
    public static interface Performer {
        public void perform(Stack<Context> contextStack, Stack<Integer> programStack);
    }

    public static class Push implements Performer {
        private int value;
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
    }

    public static class FunctionCall implements Performer {
        private String name;

        public FunctionCall(String name) {
            this.name = name;
        }

        @Override
        public void perform(Stack<Context> contextStack, Stack<Integer> programStack) {
            if (!Context.functions.containsKey(name)) {
                throw new FunctionNotFound(name, contextStack.peek().line);
            }
            Function func = Context.functions.get(name);
            if (programStack.pop() != func.params.size()) {
                throw new ArgumentNumberMismatch(name, contextStack.peek().line);
            }

            Context context = new Context();
            context.commands = func.commands;
            context.line = func.line;
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
