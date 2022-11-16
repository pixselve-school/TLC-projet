package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SpaghettiStackTest {
    @Test
    void test() {
        SpaghettiStack<Integer> stack = new SpaghettiStack<>();


        SpaghettiStack<Integer> child1 = new SpaghettiStack<>(stack);
        SpaghettiStack<Integer> child11 = new SpaghettiStack<>(child1);

        SpaghettiStack<Integer> child2 = new SpaghettiStack<>(stack);

        stack.set("a", 8);
        child1.set("x", 1);
        child11.set("a", 6);
        child2.set("p", 3);

        assertEquals(8, child1.get("a"));
        assertNull(stack.get("x"));
        assertEquals(6, child11.get("a"));
        assertNull(child2.get("x"));
        assertEquals(8, child2.get("a"));
    }
}