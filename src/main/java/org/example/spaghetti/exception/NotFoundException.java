package org.example.spaghetti.exception;

public class NotFoundException extends StackException{
    public NotFoundException(String name) {
        super(name);
    }
}
