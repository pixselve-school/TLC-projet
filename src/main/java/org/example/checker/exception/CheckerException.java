package org.example.checker.exception;

import org.antlr.runtime.tree.CommonTree;

public abstract class CheckerException extends Exception {
    public CheckerException(String message, int line, int cpos) {
        super(line + ':' + cpos + ' ' + message);
    }
    public CheckerException(CommonTree tree, String message){
        super(tree.getLine() + ':' + tree.getCharPositionInLine() + ' ' + message);
    }
}
