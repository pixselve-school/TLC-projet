package org.example;

import java.util.ArrayList;
import java.util.HashMap;

public class SpaghettiStack<T> {
    private final HashMap<String, T> hash;
    private SpaghettiStack<T> parent;
    private ArrayList<SpaghettiStack<T>> children;

    public SpaghettiStack(){
        hash = new HashMap<>();
        children = new ArrayList<>();
    }
    public SpaghettiStack(SpaghettiStack<T> parent){
        this.parent = parent;
        hash = new HashMap<>();
        parent.children.add(this);
        children = new ArrayList<>();
    }
    public void addEntity(String name, T obj){
        hash.put(name, obj);
    }
    public T get(String name){
        if(hash.containsKey(name)){
            return hash.get(name);
        }else{
            if(parent == null)
                return null;
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
