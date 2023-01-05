package org.example.checker.types;

public class FunctionType extends Type{
    String name;
    int line;
    int args;
    int rets;

    public FunctionType(String name, int line, int args, int rets) {
        this.name = name;
        this.line = line;
        this.args = args;
        this.rets = rets;
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

    public int getReturns() {
        return rets;
    }
}
