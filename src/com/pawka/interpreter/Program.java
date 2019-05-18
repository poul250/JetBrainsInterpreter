package com.pawka.interpreter;

import java.util.Stack;

public class Program {
    private Stack<Context> contextStack;
    private Stack<Integer> programStack;

    public Program() {
        contextStack = new Stack<>();
        programStack = new Stack<>();
    }

    int run(Commands commands) {
        Context initialContext = new Context();
        initialContext.commands = commands;
        contextStack.push(initialContext);

        while (!contextStack.isEmpty()) {
            Context context = contextStack.peek();
            Commands.Performer performer = context.commands.get(context.address);
            context.address += 1;
            performer.perform(contextStack, programStack);
        }

        return programStack.pop();
    }
}
