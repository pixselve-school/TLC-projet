package org.example.spaghetti;

import javax.management.InstanceNotFoundException;
import java.util.Stack;

/**
 * Spaghetti stack implementation, with a wrapper to handle it
 * @param <T>
 */
public class SpaghettiWrapper<T> {

    private final Stack<SpaghettiStack<T>> stack;

    /**
     * Create a stack
     */
    public SpaghettiWrapper(){
        stack = new Stack<>();
        stack.add(new SpaghettiStack<>());
    }

    /**
     * Get a value on the actual level and the upper levels
     * @param name of the variable
     * @return the value
     */
    public T get(String name) throws InstanceNotFoundException {
        return stack.peek().get(name);
    }

    /**
     * Add or update a value
     * @param name of the variable
     * @param obj the value
     */
    public void set(String name, T obj){
        stack.peek().set(name, obj);
    }

    /**
     * @return the level of where you are
     */
    public int getDepth(){
        return stack.size();
    }

    /**
     * Lower the level, going toward global
     */
    public void up(){
        if(stack.size() > 1)
            stack.pop();
    }

    /**
     * Grow the level, going toward blocks
     */
    public void down(){
        SpaghettiStack<T> parent = stack.peek();
        stack.add(new SpaghettiStack<T>(parent));
    }
    @Override
    public String toString(){
        return stack.get(0).toString();
    }
}
