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
import java.util.LinkedList;
import java.util.List;

public class Compiler {

    static int forCount = 0;
    static int ifCount = 0;

    public static List<String> compile(Tree tree) {
        LinkedList<String> result = new LinkedList<>();
        compile(tree, result);
        return result;
    }

    public static void reset() {
        forCount = 0;
        ifCount = 0;
        Expression.reset();
    }

    static void compile(Tree tree, List<String> current) {


        int nodeType = tree.getType();
        switch (nodeType) {
            case WhileLexer.PROGRAM -> {
                ArrayList<Tree> children = getChildren(tree);
                for (Tree child : children) {
                    compile(child, current);
                }
            }
            case WhileLexer.FUNCTION -> Function.toCode(current, tree);
            case WhileLexer.COMMANDS -> compile(tree.getChild(0), current);
            case WhileLexer.IF -> If.toCode(current, tree, ifCount++);
            case WhileLexer.LET -> Let.toCode(current, tree);
            case WhileLexer.FOR -> For.toCode(current, tree, forCount++);
            case WhileLexer.NOP -> {

            }

            default -> throw new RuntimeException("Unknown node type: " + nodeType);
        }
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
        List<String> list = compile(tree);
        for (String s : list) {
            System.out.println(s);
        }
    }
}
