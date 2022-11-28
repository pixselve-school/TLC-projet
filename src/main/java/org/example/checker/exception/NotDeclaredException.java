package org.example.checker.exception;

import org.antlr.runtime.tree.CommonTree;

public class NotDeclaredException extends CheckerException {
    public NotDeclaredException(String filename, CommonTree tree, String variableName) {
        super(filename,
            "A variable is used but not declared : " + variableName, tree.getLine(), tree.getCharPositionInLine());
    }
}
