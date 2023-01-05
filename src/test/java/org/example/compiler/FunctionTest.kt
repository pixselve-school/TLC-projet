package org.example.compiler;

import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.Tree;
import org.example.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FunctionTest {
    Tree getTreeForCode(String code) throws RecognitionException {
        Tree tree = Utils.getTreeFromString(code);
        return tree.getChild(0);
    }
    @BeforeEach
    void setUp() {
        Compiler.reset();
    }

    @Test
    void basic() throws RecognitionException {
        Tree tree = getTreeForCode("function test: read A % nop % write B");
        List<String> result = new LinkedList<>();
        Function.toCode(result, tree);
        assertArrayEquals(new String[]{
                "func begin test",
                "get A",
                "return B",
                "Return",
                "func end"
        }, result.toArray());

    }

    @Test
    void multipleReturn() throws RecognitionException {
        Tree tree = getTreeForCode("function test: read A % nop % write B, C");
        List<String> result = new LinkedList<>();
        Function.toCode(result, tree);
        assertArrayEquals(new String[]{
                "func begin test",
                "get A",
                "return B",
                "return C",
                "Return",
                "func end"
        }, result.toArray());
    }

    @Test
    void noInput() throws RecognitionException {
        Tree tree = getTreeForCode("function test: read % nop % write B");
        List<String> result = new LinkedList<>();
        Function.toCode(result, tree);
        assertArrayEquals(new String[]{
                "func begin test",
                "return B",
                "Return",
                "func end"
        }, result.toArray());
    }

    @Test
    void multipleInput() throws RecognitionException {
        Tree tree = getTreeForCode("function test: read A,B % nop % write B");
        List<String> result = new LinkedList<>();
        Function.toCode(result, tree);
        assertArrayEquals(new String[]{
                "func begin test",
                "get A",
                "get B",
                "return B",
                "Return",
                "func end"
        }, result.toArray());
    }
}
