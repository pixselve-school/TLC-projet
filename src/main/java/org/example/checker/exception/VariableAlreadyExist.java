package org.example.checker.exception;

import org.antlr.runtime.tree.CommonTree;

public class VariableAlreadyExist extends CheckerException{
    public VariableAlreadyExist(String filename, CommonTree tree, int lineDeclaratedFunction) {
        super(filename, tree, "Variable " + tree.getText() + " already exist in line " + lineDeclaratedFunction);
    }
}
