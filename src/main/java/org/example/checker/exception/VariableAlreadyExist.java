package org.example.checker.exception;

import org.antlr.runtime.tree.CommonTree;

public class VariableAlreadyExist extends CheckerException{
    public VariableAlreadyExist(CommonTree tree, int lineDeclaratedFunction) {
        super(tree, "Variable " + tree.getText() + " already exist in line " + lineDeclaratedFunction);
    }
}
