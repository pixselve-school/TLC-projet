package org.example.spaghetti;

import org.example.spaghetti.SpaghettiStack;
import org.example.spaghetti.exception.NotFoundException;
import org.example.spaghetti.exception.StackException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SpaghettiStackTest {
    @Test
    void test() throws StackException {
        SpaghettiStack<Integer> stack = new SpaghettiStack<>();


        SpaghettiStack<Integer> child1 = new SpaghettiStack<>(stack);
        SpaghettiStack<Integer> child11 = new SpaghettiStack<>(child1);

        SpaghettiStack<Integer> child2 = new SpaghettiStack<>(stack);

        stack.set("a", 8);
        child1.set("x", 1);

        assertEquals(8, child1.get("a"));
        assertThrows(NotFoundException.class, () -> stack.get("x"));
        child11.set("a", 6);
        child2.set("p", 3);
        assertEquals(6, child11.get("a"));
        assertEquals(6, child1.get("a"));
        assertThrows(NotFoundException.class, () -> child2.get("x"));
        assertEquals(6, child2.get("a"));
    }
}