package org.example.compiler

import org.antlr.runtime.RecognitionException
import org.antlr.runtime.tree.Tree
import org.example.Utils
import org.example.compiler.Compiler.reset
import org.example.compiler.Function.toCode
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

internal class FunctionTest {
    @Throws(RecognitionException::class)
    fun getTreeForCode(code: String?): Tree {
        val tree = Utils.getTreeFromString(code)
        return tree.getChild(0)
    }

    @BeforeEach
    fun setUp() {
        reset()
    }

    @Test
    @Throws(RecognitionException::class)
    fun basic() {
        val tree = getTreeForCode("function test: read A % nop % write B")
        val result = mutableListOf<String>()
        toCode(result, tree)
        Assertions.assertArrayEquals(
            arrayOf(
                "func begin test",
                "get A",
                "return B",
                "Return",
                "func end"
            ), result.toTypedArray()
        )
    }

    @Test
    @Throws(RecognitionException::class)
    fun multipleReturn() {
        val tree = getTreeForCode("function test: read A % nop % write B, C")
        val result = mutableListOf<String>()
        toCode(result, tree)
        Assertions.assertArrayEquals(
            arrayOf(
                "func begin test",
                "get A",
                "return B",
                "return C",
                "Return",
                "func end"
            ), result.toTypedArray()
        )
    }

    @Test
    @Throws(RecognitionException::class)
    fun noInput() {
        val tree = getTreeForCode("function test: read % nop % write B")
        val result = mutableListOf<String>()
        toCode(result, tree)
        Assertions.assertArrayEquals(
            arrayOf(
                "func begin test",
                "return B",
                "Return",
                "func end"
            ), result.toTypedArray()
        )
    }

    @Test
    @Throws(RecognitionException::class)
    fun multipleInput() {
        val tree = getTreeForCode("function test: read A,B % nop % write B")
        val result = mutableListOf<String>()
        toCode(result, tree)
        Assertions.assertArrayEquals(
            arrayOf(
                "func begin test",
                "get A",
                "get B",
                "return B",
                "Return",
                "func end"
            ), result.toTypedArray()
        )
    }
}
