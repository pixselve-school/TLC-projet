package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SpaghettiWrapperTest {
    @Test
    void test(){
        SpaghettiWrapper<Integer> stack = new SpaghettiWrapper<>();

        assertNull(stack.get("hello"));
        assertEquals(1, stack.getDepth());

        stack.set("x", 8);
        assertEquals(8, stack.get("x"));

        stack.down();
        assertEquals(2, stack.getDepth());
        assertEquals(8, stack.get("x"));

        stack.set("y", 10);
        assertEquals(10, stack.get("y"));
        assertEquals(8, stack.get("x"));

        stack.down();
        assertEquals(3, stack.getDepth());
        stack.set("x", 18);
        assertEquals(10, stack.get("y"));
        assertEquals(18, stack.get("x"));

        stack.up();
        assertEquals(2, stack.getDepth());
        assertEquals(10, stack.get("y"));
        assertEquals(8, stack.get("x"));

        stack.up();
        assertEquals(1, stack.getDepth());
        assertEquals(8, stack.get("x"));
        assertNull(stack.get("y"));

        stack.up();
        assertEquals(1, stack.getDepth());
        assertEquals(8, stack.get("x"));
    }
}