package org.example.checker.exception;

import org.antlr.runtime.tree.CommonTree;

public abstract class CheckerException extends Exception {
    public CheckerException(String filename, String message, int line, int cpos) {
        super(filename + ":" + line + ":" + cpos + " " + message);
    }
    public CheckerException(String filename, CommonTree tree, String message){
        super(filename + ":" + tree.getLine() + ":" + tree.getCharPositionInLine() + " " + message);
    }
}
