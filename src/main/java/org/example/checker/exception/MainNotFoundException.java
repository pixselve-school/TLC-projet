package org.example.checker.exception;

import org.antlr.runtime.tree.CommonTree;

public class MainNotFoundException extends CheckerException{
    public MainNotFoundException(String filename, CommonTree tree) {
        super(filename, tree, "The function 'main' is not present");
    }
}
