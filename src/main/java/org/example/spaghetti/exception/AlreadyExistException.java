package org.example.spaghetti.exception;

public class AlreadyExistException extends StackException{
    public AlreadyExistException(String name) {
        super(name);
    }
}
