package tui;

import tui.exceptions.EvalException;
import tui.parser.ExprTree;
import tui.parser.Parser;
import tui.parser.Value;
import tui.tokenizer.Tokenizer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Optional;
import java.util.Scanner;

public class Interpreter {
    HashMap<String, Value> memory;

    public Interpreter() {
        memory = new HashMap<>();
    }

    public void from_file(String path) throws Exception {
        System.out.println("Reading from file: " + path);
        FileReader file_reader = new FileReader(path);
        BufferedReader buff_reader = new BufferedReader(file_reader);
        String line;
        int line_number = 1;
        while ((line = buff_reader.readLine()) != null) {
            System.out.println("[" + line_number + "] " + line);
            eval(line_number, line);
            line_number++;
        }
    }

    public void repl() {
        System.out.println("Welcome to the REPL!");
        Scanner scan = new Scanner(System.in);
        String input;
        int line_number = 1;
        while (true) {
            System.out.print("[" + line_number + "] ");
            input = scan.nextLine();
            if (input.contains("exit")) {
                System.out.println("Bye!");
                return;
            }
            try {
                eval(line_number, input);
            } catch (Exception cause) {
            }
            line_number++;
        }
    }

    public Optional<Value> eval(int line_number, String input) throws Exception {
        if (input.isBlank()) {
            return Optional.empty();
        }
        Tokenizer tokens = new Tokenizer(input);
        Value output;
        try {
            ExprTree expr = Parser.parse(tokens, true);
            System.out.print("(" + line_number + ") ");
            output = expr.eval(memory);
        } catch (Exception cause) {
            EvalException eval_exception = new EvalException(line_number, cause);
            System.out.println(eval_exception.getMessage());
            throw cause;
        }
        System.out.println(output);
        return Optional.of(output);
    }

    public HashMap<String, Value> getMemory() {
        return memory;
    }
}
