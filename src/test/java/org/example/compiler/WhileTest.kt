package org.example.compiler

import org.antlr.runtime.RecognitionException
import org.antlr.runtime.tree.Tree
import org.example.Utils
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class WhileTest {
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
    fun `basic while`() {
        val tree = getTreeForCode("while A do nop od")
        val result = mutableListOf<String>()
        While.toCode(result, tree, 0)
        assertEquals(
            listOf(
                "WHILE_BEFORE_COND_0:",
                "ifz A goto WHILE_AFTER_WHILE_0",
                "goto WHILE_BEFORE_COND_0",
                "WHILE_AFTER_WHILE_0:"
            ),
            result
        )
    }

    @Test
    fun `while with body`() {
        val tree = getTreeForCode("while A do A := B od")
        val result = mutableListOf<String>()
        While.toCode(result, tree, 0)
        assertEquals(
            listOf(
                "WHILE_BEFORE_COND_0:",
                "ifz A goto WHILE_AFTER_WHILE_0",
                "A = B",
                "goto WHILE_BEFORE_COND_0",
                "WHILE_AFTER_WHILE_0:"
            ),
            result
        )
    }

    @Test
    fun `while with index greater than 0`() {
        val tree = getTreeForCode("while A do nop od")
        val result = mutableListOf<String>()
        While.toCode(result, tree, 1)
        assertEquals(
            listOf(
                "WHILE_BEFORE_COND_1:",
                "ifz A goto WHILE_AFTER_WHILE_1",
                "goto WHILE_BEFORE_COND_1",
                "WHILE_AFTER_WHILE_1:"
            ),
            result
        )
    }
}
