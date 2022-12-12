package org.example.compiler;

import org.antlr.runtime.tree.Tree;

import java.util.List;

public class Let {


    public static void toCode(List<String> result, Tree tree) {

        Tree variables = tree.getChild(0);
        Tree expressionsTree = tree.getChild(1);
        String[] variablesNames = new String[variables.getChildCount()];
        Tree[] expressions = new Tree[expressionsTree.getChildCount()];
        for (int i = 0; i < variables.getChildCount(); i++) {
            variablesNames[i] = variables.getChild(i).getText();
        }
        for (int i = 0; i < expressionsTree.getChildCount(); i++) {
            expressions[i] = expressionsTree.getChild(i);
        }


        for (int i = 0; i < variablesNames.length; i++) {
            Expression expression = new Expression(expressions[i]);
            Expression.Compose compose = expression.toCode();
            result.addAll(compose.prepend);
            result.add("%s = %s".formatted(variablesNames[i], compose.value));
        }
    }
}
