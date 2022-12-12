package org.example.compiler;

import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.Tree;
import org.example.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class LetTest {

    @BeforeEach
    void setUp() {
        Compiler.reset();
    }

    Tree getTreeForCode(String code) throws RecognitionException {
        Tree tree = Utils.getTreeFromString("function test: read A % " + code + " % write B");
        return tree.getChild(0).getChild(2).getChild(0);
    }

    @Test
    void variableEqualVariable() throws RecognitionException {
        Tree tree = getTreeForCode("A := B");
        List<String> result = new LinkedList<>();
        Let.toCode(result, tree);
        assertArrayEquals(new String[]{
                "A = B"
        }, result.toArray());

    }

    @Test
    void multipleVariablesEqualMultipleVariables() throws RecognitionException {
        Tree tree = getTreeForCode("A, B, C := D, E, F");
        List<String> result = new LinkedList<>();
        Let.toCode(result, tree);
        assertArrayEquals(new String[]{
                "A = D",
                "B = E",
                "C = F"
        }, result.toArray());
    }

    @Test
    void variableEqualNil() throws RecognitionException {
        Tree tree = getTreeForCode("A := nil");
        List<String> result = new LinkedList<>();
        Let.toCode(result, tree);
        assertArrayEquals(new String[]{
                "A = nil"
        }, result.toArray());
    }

    @Test
    void variableEqualFunctionCallNoParameters() throws RecognitionException {
        Tree tree = getTreeForCode("A := (name)");
        List<String> result = new LinkedList<>();
        Let.toCode(result, tree);
        assertArrayEquals(new String[]{
                "R_0 = call name 0",
                "A = R_0"
        }, result.toArray());
    }

    @Test
    void variableEqualFunctionCallOneParameter() throws RecognitionException {
        Tree tree = getTreeForCode("A := (name VAR1)");
        List<String> result = new LinkedList<>();
        Let.toCode(result, tree);
        assertArrayEquals(new String[]{
                "param VAR1",
                "R_0 = call name 1",
                "A = R_0"
        }, result.toArray());
    }

    @Test
    void buildEmptyTree() throws RecognitionException {
        Tree tree = getTreeForCode("A := (cons)");
        List<String> result = new LinkedList<>();
        Let.toCode(result, tree);
        assertArrayEquals(new String[]{
                "A = nil",
        }, result.toArray());
    }

    @Test
    void buildOneElementTree() throws RecognitionException {
        Tree tree = getTreeForCode("A := (cons VAR1)");
        List<String> result = new LinkedList<>();
        Let.toCode(result, tree);
        assertArrayEquals(new String[]{
                "A = VAR1",
        }, result.toArray());
    }

    @Test
    void buildTwoElementsTree() throws RecognitionException {
        Tree tree = getTreeForCode("A := (cons VAR1 VAR2)");
        List<String> result = new LinkedList<>();
        Let.toCode(result, tree);
        assertArrayEquals(new String[]{
                "R_0[0] = VAR1",
                "R_0[1] = VAR2",
                "A = R_0",
        }, result.toArray());
    }

    @Test
    void buildTreeElementsTree() throws RecognitionException {
        Tree tree = getTreeForCode("A := (cons VAR1 VAR2 VAR3)");
        List<String> result = new LinkedList<>();
        Let.toCode(result, tree);
        assertArrayEquals(new String[]{
                "R_0[1] = VAR3",
                "R_0[0] = VAR2",
                "R_1[1] = R_0",
                "R_1[0] = VAR1",
                "A = R_1",
        }, result.toArray());
    }
}
