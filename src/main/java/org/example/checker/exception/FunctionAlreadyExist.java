package org.example.checker.exception;

import org.antlr.runtime.tree.CommonTree;

public class FunctionAlreadyExist extends CheckerException{
    public FunctionAlreadyExist(String filename, CommonTree tree, int lineDeclaratedFunction) {
        super(filename, tree, "Function " + tree.getText() + " already exist in line " + lineDeclaratedFunction);
    }
}
