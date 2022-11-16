package org.example.checker.exception;

import org.antlr.runtime.tree.CommonTree;

public class FunctionAlreadyExist extends CheckerException{
    public FunctionAlreadyExist(CommonTree tree, int lineDeclaratedFunction) {
        super(tree, "Function " + tree.getText() + " already exist in line " + lineDeclaratedFunction);
    }
}
