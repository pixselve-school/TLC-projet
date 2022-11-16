package org.example.spaghetti;

import org.example.spaghetti.SpaghettiWrapper;
import org.example.spaghetti.exception.NotFoundException;
import org.example.spaghetti.exception.StackException;
import org.junit.jupiter.api.Test;

import javax.management.InstanceNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

class SpaghettiWrapperTest {
    @Test
    void test() throws InstanceNotFoundException, StackException {
        SpaghettiWrapper<Integer> stack = new SpaghettiWrapper<>();

        assertThrows(NotFoundException.class, () -> stack.get("hello"));
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
        assertEquals(18, stack.get("x"));

        stack.up();
        assertEquals(1, stack.getDepth());
        assertEquals(18, stack.get("x"));
        assertThrows(NotFoundException.class, () -> stack.get("y"));

        stack.up();
        assertEquals(1, stack.getDepth());
        assertEquals(18, stack.get("x"));
    }
}