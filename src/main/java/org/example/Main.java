package org.example;

import org.antlr.runtime.*;
import org.antlr.runtime.tree.CommonTree;
import org.example.checker.Checker;
import org.example.checker.exception.CheckerException;
import org.example.compiler.Compiler;
import org.example.optimizer.OptimizeException;
import org.example.optimizer.Optimizer;
import org.example.translator.Translator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Main {
  public static void main(String[] args) throws IOException, RecognitionException, CheckerException, OptimizeException {
    if(args.length != 0 && args.length != 2) {
      System.err.println("Usage : " + args[0] + " input_file output_file");
      System.err.println("If no arguments are passed, then \n\tinput_file = src/main/resources/and.txt\n\toutput_file = out/and.js");
    }

    String pathRead = "src/main/resources/and.txt";
    String pathWrite = "out/and.js";

    if(args.length == 2){
      pathRead = args[1];
      pathWrite = args[2];
    }

    String txt = Files.readString(Path.of(pathRead));
    CharStream cs = new ANTLRStringStream(txt);
    org.example.WhileLexer lexer = new org.example.WhileLexer(cs);
    CommonTokenStream cts = new CommonTokenStream(lexer);
    org.example.WhileParser parser = new org.example.WhileParser(cts);
    org.example.WhileParser.program_return program = parser.program();

    CommonTree tree = (CommonTree) program.getTree();

    Checker checker = new Checker(tree, Path.of(pathRead).toAbsolutePath().toString(), txt);

    checker.check();

    List<String> result = Compiler.compile(tree);

    List<String> optimized = Optimizer.optimize(result);

    Translator translator = new Translator();
    List<String> javascript = translator.translate(optimized);

    javascript.addAll(Utils.getLibs(checker.getNumberMainParam()));

    Files.writeString(Path.of(pathWrite), javascript.stream().reduce("", (String a, String b) -> a + '\n' + b));
  }
}