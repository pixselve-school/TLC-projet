package org.example.checker.exception;

import org.antlr.runtime.tree.CommonTree;
import org.example.checker.types.FunctionType;
import org.example.checker.types.Type;

public class NotDeclaredException extends CheckerException {
    public NotDeclaredException(String filename, CommonTree tree, String variableName, Class<?> type) {
        super(filename, tree,
            "A " + (type.getName().equals(FunctionType.class.getName()) ? "function" : "variable") + " is used but not declared : " + variableName);
    }
}
