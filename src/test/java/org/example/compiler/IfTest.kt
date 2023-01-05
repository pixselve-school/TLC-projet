package org.example.compiler;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;
import org.example.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.antlr.runtime.tree.Tree;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IfTest {

    @BeforeEach
    void setUp() {
        Compiler.reset();
    }

    Tree getTreeForCode(String code) throws RecognitionException {
        Tree tree = Utils.getTreeFromString("function test: read A % " + code + " % write B");
        return tree.getChild(0).getChild(2).getChild(0);
    }

    @Test
    void basic() throws RecognitionException {
        Tree tree = getTreeForCode("if Opt1 then nop else nop fi");
        List<String> result = new LinkedList<>();
        If.toCode(result, tree, 0);
        assertArrayEquals(new String[]{
                "if Opt1 goto false_label_0",
                "goto end_label_0",
                "false_label_0:",
                "end_label_0:"
        }, result.toArray());
    }

    @Test
    void withFunctionCallAsCondition() throws RecognitionException {
        Tree tree = getTreeForCode("if (not A) then nop else nop fi");
        List<String> result = new LinkedList<>();
        If.toCode(result, tree, 0);
        assertArrayEquals(new String[]{
                "param A",
                "R_0 = call not 1",
                "if R_0 goto false_label_0",
                "goto end_label_0",
                "false_label_0:",
                "end_label_0:"
        }, result.toArray());
    }

    @Test
    void withNoElse() throws RecognitionException {
        Tree tree = getTreeForCode("if Opt1 then nop fi");
        List<String> result = new LinkedList<>();
        If.toCode(result, tree, 0);
        assertArrayEquals(new String[]{
                "if Opt1 goto false_label_0",
                "false_label_0:"
        }, result.toArray());
    }
}
