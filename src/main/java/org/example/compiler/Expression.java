package org.example.compiler;

import org.antlr.runtime.tree.Tree;
import org.example.WhileParser;

import java.util.LinkedList;

public class Expression implements Element {
    private static int counter = 0;
    private final Tree tree;

    public Expression(Tree tree) {
        this.tree = tree;
    }

    public Compose toCode() {
        switch (tree.getType()) {
            case WhileParser.SYMB -> {
                // function call
                // (f v1 ... vn) -> f(v1, ..., vn)
                // first node is function name
                // other nodes are arguments

                // get function name
                String name = tree.getChild(0).getText();
                // get arguments
                LinkedList<String> args = new LinkedList<String>();
                for (int i = 1; i < tree.getChildCount(); i++) {
                    args.add(tree.getChild(i).getText());
                }
            /*
            Function call have format :
            param v1
            param v2
            ...
            param vn
            R_1 = call function_name n
             */
                StringBuilder argumentsString = new StringBuilder();
                for (String arg : args) {
                    argumentsString.append("param ").append(arg).append("\n");
                }
                argumentsString.append("R_").append(counter).append(" = call ").append(name).append(" ").append(args.size());
                Compose result = new Compose(argumentsString.toString(), "R_%d".formatted(counter));
                counter++;
                return result;
            }
            case WhileParser.Variable -> {
                return new Compose("", tree.getText());
            }
            case WhileParser.NIL -> {
                return new Compose("", "nil");
            }
            case WhileParser.CONS -> {
                if (tree.getChildCount() == 0) {
                    return new Compose("", "nil");
                } else if (tree.getChildCount() == 1) {
                    return new Compose("", tree.getChild(0).getText());
                } else if (tree.getChildCount() == 2) {
                    Compose first = new Expression(tree.getChild(0)).toCode();
                    Compose second = new Expression(tree.getChild(1)).toCode();

                    StringBuilder prepend = new StringBuilder();
                    prepend.append(first.prepend).append("\n");
                    prepend.append(second.prepend).append("\n");
                    prepend.append("R_").append(counter).append("[0] = ").append(first.value).append("\n");
                    prepend.append("R_").append(counter).append("[1] = ").append(second.value).append("\n");
                    Compose result = new Compose(prepend.toString(), "R_%d".formatted(counter));
                    counter++;
                    return result;
                } else {
                    throw new RuntimeException("NOT IMPLEMENTED");
                }
            }
            default -> {
                throw new RuntimeException("NOT IMPLEMENTED");
            }
        }
    }

    public static class Compose {
        public String prepend;
        public String value;

        public Compose(String prepend, String value) {
            this.prepend = prepend;
            this.value = value;
        }
    }
}
