package org.example.compiler

import org.antlr.runtime.RecognitionException
import org.antlr.runtime.tree.Tree
import org.example.Utils
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ForTest {
    @BeforeEach
    fun setUp() {
        Compiler.reset()
    }

    @Throws(RecognitionException::class)
    fun getTreeForCode(code: String): Tree {
        val tree = Utils.getTreeFromString("function test: read A % $code % write B")
        return tree.getChild(0).getChild(2).getChild(0)
    }

    @Test
    fun `basic for`() {
        val tree = getTreeForCode("for A do nop od")
        val result = mutableListOf<String>()
        For.toCode(result, tree, 0)
        println(result)
        assertEquals(
            listOf(
                "FOR_VARIABLE_0 = A",
                "FOR_BEFORE_COND_0:",
                "FOR_VARIABLE_COND_0 = FOR_VARIABLE_0 <= 0",
                "if FOR_VARIABLE_COND_0 goto FOR_AFTER_FOR_0",
                "FOR_VARIABLE_0 = FOR_VARIABLE_0[1]",
                "JMP FOR_BEFORE_COND_0",
                "FOR_AFTER_FOR_0:"
            ),
            result
        )
    }

    @Test
    fun `for with body`() {
        val tree = getTreeForCode("for A do A := B od")
        val result = mutableListOf<String>()
        For.toCode(result, tree, 0)
        println(result)
        assertEquals(
            listOf(
                "FOR_VARIABLE_0 = A",
                "FOR_BEFORE_COND_0:",
                "FOR_VARIABLE_COND_0 = FOR_VARIABLE_0 <= 0",
                "if FOR_VARIABLE_COND_0 goto FOR_AFTER_FOR_0",
                "A = B",
                "FOR_VARIABLE_0 = FOR_VARIABLE_0[1]",
                "JMP FOR_BEFORE_COND_0",
                "FOR_AFTER_FOR_0:"
            ),
            result
        )
    }
}
