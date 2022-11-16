package org.example.checker.exception;

import org.antlr.runtime.tree.CommonTree;

public class UnhandledException extends CheckerException{
    public UnhandledException(CommonTree tree) {
        super(tree, "Unhandled exception on leaf " + tree.getText());
    }
}
