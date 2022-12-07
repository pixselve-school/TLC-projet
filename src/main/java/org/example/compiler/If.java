package org.example.compiler;

import org.antlr.runtime.tree.Tree;

public class If implements Element {
    protected static int ifCount = 0;

    private final Tree condition;
    private final Tree thenBranch;
    private final Tree elseBranch;

    public If(Tree tree) {
        condition = tree.getChild(0);
        thenBranch = tree.getChild(1);
        elseBranch = tree.getChild(2);
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        Expression condition = new Expression(this.condition);
        Expression.Compose compose = condition.toCode();
        result.append(compose.prepend).append("\n");

        if (elseBranch == null) {
            result.append("ifz ").append(compose.value).append(" goto false_label_").append(ifCount).append("\n");
            result.append(Compiler.compile(thenBranch));
            result.append("false_label_").append(ifCount).append(":\n");
        } else {
            result.append("ifz ").append(compose.value).append(" goto false_label_").append(ifCount).append("\n");
            result.append(Compiler.compile(thenBranch));
            result.append("goto end_label_").append(ifCount).append("\n");
            result.append("false_label_").append(ifCount).append(":\n");
            result.append(Compiler.compile(elseBranch));
            result.append("end_label_").append(ifCount).append(":\n");

        }
        ifCount++;

        return result.toString();
    }
}
