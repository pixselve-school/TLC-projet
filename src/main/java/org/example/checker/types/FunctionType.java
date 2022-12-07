package org.example.checker.types;

public class FunctionType extends Type{
    String name;
    int line;
    int args;

    public FunctionType(String name, int line, int args) {
        this.name = name;
        this.line = line;
        this.args = args;
    }

    public String getName() {
        return name;
    }

    public int getLine() {
        return line;
    }

    public int getArgs() {
        return args;
    }
}
