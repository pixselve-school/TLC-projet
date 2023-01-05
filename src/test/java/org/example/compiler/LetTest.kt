package org.example.compiler

import org.antlr.runtime.RecognitionException
import org.antlr.runtime.tree.Tree
import org.example.Utils
import org.example.compiler.Compiler.reset
import org.example.compiler.Let.toCode
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.util.*

internal class LetTest {
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
    fun `should associate a variable to another variable`() {
        val tree = getTreeForCode("A := B")
        val result = mutableListOf<String>()
        toCode(result, tree)
        Assertions.assertArrayEquals(
            arrayOf(
                "A = B"
            ), result.toTypedArray()
        )
    }

    @Test
    @Throws(RecognitionException::class)
    fun `should associate multiple variables to multiple variables`() {
        val tree = getTreeForCode("A, B, C := D, E, F")
        val result = mutableListOf<String>()
        toCode(result, tree)
        Assertions.assertArrayEquals(
            arrayOf(
                "A = D",
                "B = E",
                "C = F"
            ), result.toTypedArray()
        )
    }

    @Test
    @Throws(RecognitionException::class)
    fun `should be nil`() {
        val tree = getTreeForCode("A := nil")
        val result = mutableListOf<String>()
        toCode(result, tree)
        Assertions.assertArrayEquals(
            arrayOf(
                "A = nil"
            ), result.toTypedArray()
        )
    }

    @Test
    @Throws(RecognitionException::class)
    fun variableEqualFunctionCallNoParameters() {
        val tree = getTreeForCode("A := (name)")
        val result = mutableListOf<String>()
        toCode(result, tree)
        Assertions.assertArrayEquals(
            arrayOf(
                "R_0 = call name 0",
                "R_1 = R_0[0]",
                "A = R_1"
            ), result.toTypedArray()
        )
    }

    @Test
    @Throws(RecognitionException::class)
    fun variableEqualFunctionCallOneParameter() {
        val tree = getTreeForCode("A := (name VAR1)")
        val result = mutableListOf<String>()
        toCode(result, tree)
        Assertions.assertArrayEquals(
            arrayOf(
                "param VAR1",
                "R_0 = call name 1",
                "R_1 = R_0[0]",
                "A = R_1"
            ), result.toTypedArray()
        )
    }

    @Test
    @Throws(RecognitionException::class)
    fun buildEmptyTree() {
        val tree = getTreeForCode("A := (cons)")
        val result = mutableListOf<String>()
        toCode(result, tree)
        Assertions.assertArrayEquals(
            arrayOf(
                "A = nil"
            ), result.toTypedArray()
        )
    }

    @Test
    @Throws(RecognitionException::class)
    fun buildOneElementTree() {
        val tree = getTreeForCode("A := (cons VAR1)")
        val result = mutableListOf<String>()
        toCode(result, tree)
        Assertions.assertArrayEquals(
            arrayOf(
                "A = VAR1"
            ), result.toTypedArray()
        )
    }

    @Test
    @Throws(RecognitionException::class)
    fun buildTwoElementsTree() {
        val tree = getTreeForCode("A := (cons VAR1 VAR2)")
        val result = mutableListOf<String>()
        toCode(result, tree)
        Assertions.assertArrayEquals(
            arrayOf(
                "R_0[0] = VAR1",
                "R_0[1] = VAR2",
                "A = R_0"
            ), result.toTypedArray()
        )
    }

    @Test
    @Throws(RecognitionException::class)
    fun buildTreeElementsTree() {
        val tree = getTreeForCode("A := (cons VAR1 VAR2 VAR3)")
        val result = mutableListOf<String>()
        toCode(result, tree)
        Assertions.assertArrayEquals(
            arrayOf(
                "R_0[1] = VAR3",
                "R_0[0] = VAR2",
                "R_1[1] = R_0",
                "R_1[0] = VAR1",
                "A = R_1"
            ), result.toTypedArray()
        )
    }

    @Test
    @Throws(RecognitionException::class)
    fun buildString() {
        val tree = getTreeForCode("A := (cons (cons (cons (cons ceci est) une) liste) nil)")
        val result = mutableListOf<String>()
        toCode(result, tree)
        Assertions.assertArrayEquals(
            arrayOf(
                "R_0[0] = ceci",
                "R_0[1] = est",
                "R_1[0] = R_0",
                "R_1[1] = une",
                "R_2[0] = R_1",
                "R_2[1] = liste",
                "R_3[0] = R_2",
                "R_3[1] = nil",
                "A = R_3"
            ), result.toTypedArray()
        )
    }

    @Nested
    internal inner class ListTest {
        @Test
        @Throws(RecognitionException::class)
        fun empty() {
            val tree = getTreeForCode("A := (list)")
            val result = mutableListOf<String>()
            toCode(result, tree)
            Assertions.assertArrayEquals(
                arrayOf(
                    "A = nil"
                ), result.toTypedArray()
            )
        }

        @Test
        @Throws(RecognitionException::class)
        fun oneElement() {
            val tree = getTreeForCode("A := (list VAR1)")
            val result = mutableListOf<String>()
            toCode(result, tree)
            Assertions.assertArrayEquals(
                arrayOf(
                    "R_0[0] = VAR1",
                    "R_0[1] = nil",
                    "A = R_0"
                ), result.toTypedArray()
            )
        }

        @Test
        @Throws(RecognitionException::class)
        fun twoElements() {
            val tree = getTreeForCode("A := (list VAR1 VAR2)")
            val result = mutableListOf<String>()
            toCode(result, tree)
            println(result)
            Assertions.assertArrayEquals(
                arrayOf(
                    "R_0[1] = nil",
                    "R_0[0] = VAR2",
                    "R_1[0] = VAR1",
                    "R_1[1] = R_0",
                    "A = R_1"
                ), result.toTypedArray()
            )
        }

        @Test
        @Throws(RecognitionException::class)
        fun threeElements() {
            val tree = getTreeForCode("A := (list VAR1 VAR2 VAR3)")
            val result = mutableListOf<String>()
            toCode(result, tree)
            println(result)
            Assertions.assertArrayEquals(
                arrayOf(
                    "R_0[1] = nil",
                    "R_0[0] = VAR3",
                    "R_1[0] = VAR2",
                    "R_1[1] = R_0",
                    "R_2[0] = VAR1",
                    "R_2[1] = R_1",
                    "A = R_2"
                ), result.toTypedArray()
            )
        }
    }

    @Nested
    internal inner class HdTest {
        @Test
        @Throws(RecognitionException::class)
        fun nil() {
            val tree = getTreeForCode("A := (hd nil)")
            val result = mutableListOf<String>()
            toCode(result, tree)
            Assertions.assertArrayEquals(
                arrayOf(
                    "A = nil"
                ), result.toTypedArray()
            )
        }

        @Test
        @Throws(RecognitionException::class)
        fun oneElement() {
            val tree = getTreeForCode("A := (hd VAR1)")
            val result = mutableListOf<String>()
            toCode(result, tree)
            Assertions.assertArrayEquals(
                arrayOf(
                    "param VAR1",
                    "R_0 = call hd 1",
                    "R_1 = R_0[0]",
                    "A = R_1"
                ), result.toTypedArray()
            )
        }
    }

    @Nested
    internal inner class TlTest {
        @Test
        @Throws(RecognitionException::class)
        fun nil() {
            val tree = getTreeForCode("A := (tl nil)")
            val result = mutableListOf<String>()
            toCode(result, tree)
            Assertions.assertArrayEquals(
                arrayOf(
                    "A = nil"
                ), result.toTypedArray()
            )
        }

        @Test
        @Throws(RecognitionException::class)
        fun oneElement() {
            val tree = getTreeForCode("A := (tl VAR1)")
            val result = mutableListOf<String>()
            toCode(result, tree)
            println(result)
            Assertions.assertArrayEquals(
                arrayOf(
                    "param VAR1",
                    "R_0 = call tl 1",
                    "R_1 = R_0[0]",
                    "A = R_1"
                ), result.toTypedArray()
            )
        }
    }
}
