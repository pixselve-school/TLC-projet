package org.example.checker.types;

public class FunctionType extends Type{
    String name;
    int line;

    public FunctionType(String name, int line) {
        this.name = name;
        this.line = line;
    }

    public String getName() {
        return name;
    }

    public int getLine() {
        return line;
    }
}
