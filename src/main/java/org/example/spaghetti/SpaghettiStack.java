package org.example.spaghetti;

import javax.management.InstanceNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Tree class used internaly for stack.
 * The stack has a parent, and if it does not find the variable, it searches on the parent
 * @param <T>
 */
class SpaghettiStack<T> {
    private final HashMap<String, T> hash;
    private SpaghettiStack<T> parent;
    private final ArrayList<SpaghettiStack<T>> children;

    /**
     * Create a stack with no parent
     */
    public SpaghettiStack(){
        hash = new HashMap<>();
        children = new ArrayList<>();
    }

    /**
     * Create a stack with a parent to look for variables
     * @param parent
     */
    public SpaghettiStack(SpaghettiStack<T> parent){
        this.parent = parent;
        hash = new HashMap<>();
        parent.children.add(this);
        children = new ArrayList<>();
    }

    /**
     * Add or modify a value
     * @param name of the variable
     * @param obj value
     */
    public void set(String name, T obj){
        hash.put(name, obj);
    }

    /**
     * Search the value on this level and upper level
     * @param name of the variable
     * @return the value
     * @throws InstanceNotFoundException if the value is not found
     */
    public T get(String name) throws InstanceNotFoundException {
        if(hash.containsKey(name)){
            return hash.get(name);
        }else{
            if(parent == null)
                throw new InstanceNotFoundException(name);
            return parent.get(name);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        toString(0, sb);
        return sb.toString();
    }
    private void toString(int tab, StringBuilder sb){
        hash.forEach((s, t) -> {
            sb.append("\t".repeat(tab));
            sb.append(s).append(" -> ").append(t).append('\n');
        });
        children.forEach(stack -> {
            stack.toString(tab+1, sb);
        });
    }
}
