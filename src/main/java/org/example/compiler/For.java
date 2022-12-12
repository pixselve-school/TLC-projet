package org.example.compiler;

import org.antlr.runtime.tree.Tree;

import java.util.List;


/**
 * ┌───────┐
 * │FOR    │
 * └┬─────┬┘
 * ┌▽───┐┌▽───────┐
 * │Opt1││COMMANDS│
 * └────┘└────────┘
 */
public class For {


    public static void toCode(List<String> result, Tree tree, int index) {
        Tree variable = tree.getChild(0);
        Tree commands = tree.getChild(1);

        result.add("for_label_%d:".formatted(index));
        result.add("if %s goto end_for_label_%d".formatted(variable.getText(), index));
        Compiler.compile(commands, result);
        result.add("goto for_label_%d".formatted(index));
        result.add("end_for_label_%d:".formatted(index));
    }

}
