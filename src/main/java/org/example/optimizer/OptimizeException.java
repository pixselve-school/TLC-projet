package org.example.optimizer;

public class OptimizeException extends Exception{
    public OptimizeException(String line, String message) {
        super("Translator exception : " + message + "\n\t" + line);
    }
}
