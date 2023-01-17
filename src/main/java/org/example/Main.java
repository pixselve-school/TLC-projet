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
    if(args.length != 0 && args.length != 3) {
      System.err.println("Usage : " + args[0] + " input_file output_3addr output_js");
      System.err.println("If no arguments are passed, then \n" +
              "\tinput_file = src/main/resources/and.txt\n" +
              "\toutput_3addr = src/main/ressources/and.3addr\n" +
              "\toutput_js = out/and.js");
    }

    String pathRead = "src/main/resources/and.txt";
    String pathWrite3addr = "out/and.3addr";
    String pathWriteJS = "out/and.js";

    if(args.length == 3){
      pathRead = args[0];
      pathWrite3addr = args[1];
      pathWriteJS = args[2];
    }

    // Open file
    System.out.print("Reading " + pathRead + "...");
    String txt = Files.readString(Path.of(pathRead));
    System.out.println("\tdone : " + txt.length() + " bytes");

    // Parse with antlr
    System.out.print("Parsing language...");
    CharStream cs = new ANTLRStringStream(txt);
    org.example.WhileLexer lexer = new org.example.WhileLexer(cs);
    CommonTokenStream cts = new CommonTokenStream(lexer);
    org.example.WhileParser parser = new org.example.WhileParser(cts);
    org.example.WhileParser.program_return program = parser.program();
    System.out.println("\tdone");

    // Get the AST
    CommonTree tree = (CommonTree) program.getTree();

    // Check the semantic
    System.out.print("Checking...");
    Checker checker = new Checker(tree, Path.of(pathRead).toAbsolutePath().toString(), txt);
    System.out.println("\tdone");
    if(!checker.check()){
      System.err.println("Error during compilation, aborting");
      return;
    }

    // Compile to 3addr
    System.out.print("Compiling...");
    List<String> result = Compiler.compile(tree);
    System.out.println("\tdone");

    // Optimize 3addr
    System.out.print("Optimizing...");
    List<String> optimized = Optimizer.optimize(result);
    System.out.println("\tdone");

    // Save 3addr
    System.out.print("Saving 3addr to " + pathWrite3addr  + "...");
    Files.writeString(Path.of(pathWrite3addr), optimized.stream().reduce("", (String a, String b) -> a + '\n' + b));
    System.out.println("\tdone");

    // Translate to JS
    System.out.print("Translating...");
    Translator translator = new Translator();
    List<String> javascript = translator.translate(optimized);
    System.out.println("\tdone");

    // Add while std lib
    System.out.print("Adding libraries");
    javascript.addAll(Utils.getLibs(checker.getNumberMainParam()));
    System.out.println("\tdone");

    // Save JS
    System.out.print("Saving JS to " + pathWriteJS + "...");
    Files.writeString(Path.of(pathWriteJS), javascript.stream().reduce("", (String a, String b) -> a + '\n' + b));
    System.out.println("\tdone");
  }
}