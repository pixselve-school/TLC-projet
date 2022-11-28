package org.example.checker.exception;

import org.antlr.runtime.tree.CommonTree;

public class UnhandledException extends CheckerException{
    public UnhandledException(String filename, CommonTree tree) {
        super(filename, tree, "Unhandled exception on leaf " + tree.getText());
    }
}
