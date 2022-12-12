package org.example.compiler;

import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;
import org.example.WhileLexer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.example.compiler.Compiler.compile;
import static org.example.compiler.Compiler.getChildren;

public class Function implements Element {


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

    public static void toCode(List<String> result, Tree tree) {

        String name = tree.getChild(0).getText();
        Tree output = tree.getChild(3);
        LinkedList<String> outputParsed = parseOutput(output);
        Tree body = tree.getChild(2);


        result.add("func begin %s".formatted(name));
        compile(body, result);
        for (String s : outputParsed) {
            result.add("return " + s);
        }
        result.add("Return");
        result.add("func end");
    }
}
