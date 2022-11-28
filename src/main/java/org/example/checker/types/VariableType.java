package org.example.checker.types;

public class VariableType extends Type {
    String name;
    int line;

    public String getName() {
        return name;
    }

    public int getLine() {
        return line;
    }

    public VariableType(String name, int line) {
        this.name = name;
        this.line = line;
    }
}
