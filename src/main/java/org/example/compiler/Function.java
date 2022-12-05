package org.example.compiler;

import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;
import org.example.WhileLexer;

import java.util.ArrayList;
import java.util.LinkedList;

import static org.example.compiler.Compiler.compile;
import static org.example.compiler.Compiler.getChildren;

public class Function implements Element {
    public String name;
    public LinkedList<String> output;

    public Tree body;


    public Function(Tree tree) {
        this.name = tree.getChild(0).getText();
        Tree output = tree.getChild(3);
        this.output = parseOutput(output);
        this.body = tree.getChild(2);
    }

    public static LinkedList<String> parseOutput(Tree tree) {
        if (tree.getType() != WhileLexer.OUTPUTS) {
            throw new RuntimeException("Expected OUTPUTS node");
        }
        LinkedList<String> result = new LinkedList<>();
        ArrayList<Tree> outputs = getChildren(tree);
        for (Tree output : outputs) {
            result.add(output.getText());
        }
        return result;
    }

    private String outputToString() {
        StringBuilder builder = new StringBuilder();
        LinkedList<String> strings = this.output;
        for (int i = 0, stringsSize = strings.size(); i < stringsSize; i++) {
            String s = strings.get(i);
            builder.append("return ").append(s);
            if (i < stringsSize - 1) {
                builder.append("\n");
            }
        }
        return builder.toString();
    }

    private String bodyToString() {
        if (this.body.getType() != WhileLexer.COMMANDS) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < this.body.getChildCount(); i++) {
            result.append(compile(this.body.getChild(i)));
        }
        return result.toString();
    }

    public String toString() {
        return "func begin %s\n%s\n%s\nReturn\nfunc end".formatted(name, this.bodyToString(), this.outputToString());
    }
}
