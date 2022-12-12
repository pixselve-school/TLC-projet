package org.example.compiler;

import org.antlr.runtime.tree.Tree;

import java.util.LinkedList;
import java.util.List;

public class If implements Element {

    public static void toCode(List<String> result, Tree tree, int index) {

        Tree condition = tree.getChild(0);
        Tree thenBranch = tree.getChild(1);
        Tree elseBranch = tree.getChild(2);

        Expression conditionExp = new Expression(condition);
        Expression.Compose compose = conditionExp.toCode();
        result.addAll(compose.prepend);
        result.add("if %s goto false_label_%d".formatted(compose.value, index));
        Compiler.compile(thenBranch, result);
        if (elseBranch == null) {
            result.add("false_label_%d:".formatted(index));
        } else {
            result.add("goto end_label_%d".formatted(index));
            result.add("false_label_%d:".formatted(index));
            Compiler.compile(elseBranch, result);
            result.add("end_label_%d:".formatted(index));

        }
    }

}
