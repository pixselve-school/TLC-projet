package org.example.compiler;

import org.antlr.runtime.tree.Tree;
import org.example.WhileLexer;
import org.example.WhileParser;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

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
                LinkedList<String> args = new LinkedList<>();
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
                LinkedList<String> prepend = new LinkedList<>();
                for (String arg : args) {
                    prepend.add("param " + arg);
                }
                prepend.add("R_%d = call %s %d".formatted(counter, name, args.size()));
                Compose result = new Compose(prepend, "R_%d".formatted(counter));
                counter++;
                return result;
            }
            case WhileParser.Variable -> {
                return new Compose(Collections.emptyList(), tree.getText());
            }
            case WhileParser.NIL -> {
                return new Compose(Collections.emptyList(), "nil");
            }
            case WhileParser.CONS -> {
                if (tree.getChildCount() == 0) {
                    return new Compose(Collections.emptyList(), "nil");
                } else if (tree.getChildCount() == 1) {
                    return new Compose(Collections.emptyList(), tree.getChild(0).getText());
                } else if (tree.getChildCount() == 2) {
                    Compose first = new Expression(tree.getChild(0)).toCode();
                    Compose second = new Expression(tree.getChild(1)).toCode();
                    LinkedList<String> prepend = new LinkedList<>();
                    prepend.addAll(first.prepend);
                    prepend.addAll(second.prepend);
                    prepend.add("R_%d[0] = %s".formatted(counter, first.value));
                    prepend.add("R_%d[1] = %s".formatted(counter, second.value));

                    Compose result = new Compose(prepend, "R_%d".formatted(counter));
                    counter++;
                    return result;
                } else {
                    throw new RuntimeException("NOT IMPLEMENTED");
                }
            }
            case WhileParser.TL -> {
                Compose compose = new Expression(tree.getChild(0)).toCode();
                LinkedList<String> prepend = new LinkedList<>(compose.prepend);
                prepend.add("R_%d = %s[0]".formatted(counter, compose.value));
                Compose result = new Compose(prepend, "R_%d".formatted(counter));
                counter++;
                return result;
            }
            default -> {
                throw new RuntimeException("NOT IMPLEMENTED");
            }
        }
    }

    public static class Compose {
        public List<String> prepend;
        public String value;

        public Compose(List<String> prepend, String value) {
            this.prepend = prepend;
            this.value = value;
        }
    }
}
