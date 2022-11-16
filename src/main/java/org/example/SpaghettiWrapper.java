package org.example;

import java.util.Stack;

public class SpaghettiWrapper<T> {

    private final Stack<SpaghettiStack<T>> stack;

    public SpaghettiWrapper(){
        stack = new Stack<>();
        stack.add(new SpaghettiStack<>());
    }
    public T get(String name){
        return stack.peek().get(name);
    }
    public void set(String name, T obj){
        stack.peek().set(name, obj);
    }

    public int getDepth(){
        return stack.size();
    }

    public void up(){
        if(stack.size() > 1)
            stack.pop();
    }
    public void down(){
        SpaghettiStack<T> parent = stack.peek();
        stack.add(new SpaghettiStack<T>(parent));
    }
}
