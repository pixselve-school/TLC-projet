package org.example.compiler;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;
import org.example.WhileLexer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class Compiler {

  private static int ifCount = 0;

  public static StringBuilder compile(Tree tree) {
    int nodeType = tree.getType();
    return switch (nodeType) {
      case WhileLexer.PROGRAM -> compile(tree.getChild(0));
      case WhileLexer.FUNCTION -> compileFunction(tree);
      case WhileLexer.IF -> compileIf(tree);
      default -> new StringBuilder("# TODO\n");
    };
  }

  public static StringBuilder compileIf(Tree tree) {
    StringBuilder result = new StringBuilder();
    result.append("ifz COND goto false_label_").append(ifCount).append("\n");
    result.append(compile(tree.getChild(0)));
    result.append("goto end_label_").append(ifCount).append("\n");
    result.append("false_label_").append(ifCount).append(":\n");
    result.append(compile(tree.getChild(1)));
    result.append("end_label_").append(ifCount).append(":\n");
    ifCount++;
    return result;
  }

  public static StringBuilder compileFunction(Tree tree) {
    System.out.println("🪜 Compiling function");
    String functionName = tree.getChild(0).getText();
    Tree args = tree.getChild(1);
    Tree body = tree.getChild(2);
    Tree output = tree.getChild(3);

    StringBuilder result = new StringBuilder();
    result.append("func begin ").append(functionName).append("\n");
    result.append(compileBody(body)).append("\n");
    result.append(compileReturn(output));
    result.append("func end").append("\n");


    return result;
  }

  public static StringBuilder compileBody(Tree tree) {
    if (tree.getType() != WhileLexer.COMMANDS) {
      return new StringBuilder();
    }
    StringBuilder result = new StringBuilder();
    for (int i = 0; i < tree.getChildCount(); i++) {
      result.append(compile(tree.getChild(i)));
    }
    return result;
  }

  public static String compileReturn(Tree tree) {
    System.out.println("🪜 Compiling return");

    if (tree.getType() != WhileLexer.OUTPUTS) {
      throw new RuntimeException("Expected OUTPUTS node");
    }

    StringBuilder result = new StringBuilder();
    ArrayList<Tree> outputs = getChildren(tree);
    for (Tree output : outputs) {
      result.append("return ").append(output.getText()).append("\n");
    }
    return result.toString();
  }

  public static StringBuilder compileArgs(Tree tree) {
    if (tree.getType() != WhileLexer.INPUTS) {
      throw new RuntimeException("Expected inputs");
    }
    StringBuilder builder = new StringBuilder();
    ArrayList<Tree> args = getChildren(tree.getChild(0));
    for (int i = 0; i < args.size(); i++) {
      Tree arg = args.get(i);
      builder.append(arg.getText());
      if (i < args.size() - 1) {
        builder.append(", ");
      }
    }
    return builder;
  }

  /**
   * Get children of a node.
   *
   * @param tree The node.
   * @return The children.
   */
  public static ArrayList<Tree> getChildren(Tree tree) {
    ArrayList<Tree> children = new ArrayList<>();
    int i = 0;
    while (true) {
      Tree child = tree.getChild(i);
      if (child == null) {
        break;
      }
      children.add(child);
      i++;
    }
    return children;
  }

  public static void main(String[] args) throws IOException, RecognitionException {
    String path = "src/main/resources/and.txt";
    String txt = Files.readString(Path.of(path));
    CharStream cs = new ANTLRStringStream(txt);
    org.example.WhileLexer lexer = new org.example.WhileLexer(cs);
    CommonTokenStream cts = new CommonTokenStream(lexer);
    org.example.WhileParser parser = new org.example.WhileParser(cts);
    org.example.WhileParser.program_return program = parser.program();

    CommonTree tree = (CommonTree) program.getTree();
    String result = compile(tree).toString();
    System.out.println(result);
  }
}
