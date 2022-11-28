package org.example;

import org.antlr.runtime.*;
import org.antlr.runtime.tree.CommonTree;
import org.example.checker.Checker;
import org.example.checker.exception.CheckerException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
  public static void main(String[] args) throws IOException, RecognitionException, CheckerException {
    System.out.println("Hello world!");

    String path = "src/main/resources/and.txt";
    String txt = Files.readString(Path.of(path));
    CharStream cs = new ANTLRStringStream(txt);
    org.example.WhileLexer lexer = new org.example.WhileLexer(cs);
    CommonTokenStream cts = new CommonTokenStream(lexer);
    org.example.WhileParser parser = new org.example.WhileParser(cts);
    org.example.WhileParser.program_return program = parser.program();

    CommonTree tree = (CommonTree) program.getTree();

    Checker checker = new Checker(tree, Path.of(path).toAbsolutePath().toString());

    checker.check();

    System.out.println(tree.toStringTree());
  }
}