package tui;

import tui.interpreter.Interpreter;

public class Main {
    public static void main(String[] args) throws Exception {
        // get the path to a file from the command line, else run repl
        Interpreter interpreter = new Interpreter();
        if (args.length > 0) {
            String path = args[0];
            interpreter.from_file(path);
        } else {
            interpreter.repl();
        }
    }

}