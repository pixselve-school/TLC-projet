package org.example.compiler

import org.antlr.runtime.RecognitionException
import org.antlr.runtime.tree.Tree
import org.example.Utils
import org.example.compiler.Compiler.reset
import org.example.compiler.If.toCode
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

internal class IfTest {
    @BeforeEach
    fun setUp() {
        reset()
    }

    @Throws(RecognitionException::class)
    fun getTreeForCode(code: String): Tree {
        val tree = Utils.getTreeFromString("function test: read A % $code % write B")
        return tree.getChild(0).getChild(2).getChild(0)
    }

    @Test
    @Throws(RecognitionException::class)
    fun basic() {
        val tree = getTreeForCode("if Opt1 then nop else nop fi")
        val result = mutableListOf<String>()
        toCode(result, tree, 0)
        Assertions.assertArrayEquals(
            arrayOf(
                "if Opt1 goto false_label_0",
                "goto end_label_0",
                "false_label_0:",
                "end_label_0:"
            ), result.toTypedArray()
        )
    }

    @Test
    @Throws(RecognitionException::class)
    fun withFunctionCallAsCondition() {
        val tree = getTreeForCode("if (not A) then nop else nop fi")
        val result = mutableListOf<String>()
        toCode(result, tree, 0)
        println(result)
        Assertions.assertArrayEquals(
            arrayOf(
                "param A",
                "R_0 = call not 1",
                "R_1 = R_0[0]",
                "if R_1 goto false_label_0",
                "goto end_label_0",
                "false_label_0:",
                "end_label_0:"
            ), result.toTypedArray()
        )
    }

    @Test
    @Throws(RecognitionException::class)
    fun withNoElse() {
        val tree = getTreeForCode("if Opt1 then nop fi")
        val result = mutableListOf<String>()
        toCode(result, tree, 0)
        Assertions.assertArrayEquals(
            arrayOf(
                "if Opt1 goto false_label_0",
                "false_label_0:"
            ), result.toTypedArray()
        )
    }
}
