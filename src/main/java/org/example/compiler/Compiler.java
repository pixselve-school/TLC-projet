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
    public static StringBuilder compile(Tree tree) {
        int nodeType = tree.getType();
        return switch (nodeType) {
            case WhileLexer.PROGRAM -> {
                StringBuilder result = new StringBuilder();
                ArrayList<Tree> children = getChildren(tree);
                for (Tree child : children) {
                    result.append(compile(child));
                    result.append("\n");
                }
                yield result;
            }
            case WhileLexer.FUNCTION -> compileFunction(tree);
            case WhileLexer.IF -> compileIf(tree);
            default -> new StringBuilder("# TODO\n");
        };
    }

    public static StringBuilder compileIf(Tree tree) {
        If ifStatement = new If(tree);
        return new StringBuilder(ifStatement.toString());
    }


    public static StringBuilder compileFunction(Tree tree) {
        Function function = new Function(tree);
        return new StringBuilder(function.toString());
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
        System.out.println("------------------");
        System.out.println(result);
    }
}
