package org.example.optimizer;

public class NumberOfArgumentException extends OptimizeException{
    public NumberOfArgumentException(String line) {
        super(line, "Wrong number of arguments");
    }
}

