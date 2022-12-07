package org.example.compiler;

import org.antlr.runtime.tree.Tree;

public class Let {

    public String[] variablesNames;

    public Tree[] expressions;

    public Let(Tree tree) {
        Tree variables = tree.getChild(0);
        Tree expressions = tree.getChild(1);
        this.variablesNames = new String[variables.getChildCount()];
        this.expressions = new Tree[expressions.getChildCount()];
        for (int i = 0; i < variables.getChildCount(); i++) {
            this.variablesNames[i] = variables.getChild(i).getText();
        }
        for (int i = 0; i < expressions.getChildCount(); i++) {
            this.expressions[i] = expressions.getChild(i);
        }

    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < variablesNames.length; i++) {
            Expression expression = new Expression(expressions[i]);
            Expression.Compose compose = expression.toCode();
            result.append(compose.prepend).append("\n");
            result.append(variablesNames[i]);
            result.append(" = ");
            result.append(compose.value);
            result.append("\n");
        }
        return result.toString();
    }
}
