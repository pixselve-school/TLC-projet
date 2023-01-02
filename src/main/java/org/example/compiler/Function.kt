package org.example.compiler

import org.antlr.runtime.tree.Tree
import org.example.WhileLexer

import java.util.*

object Function : Element {
    private fun parseOutput(tree: Tree): LinkedList<String> {
        if (tree.type != WhileLexer.OUTPUTS) {
            throw RuntimeException("Expected OUTPUTS node")
        }
        val result = LinkedList<String>()
        val outputs = Compiler.getChildren(tree)
        for (output in outputs) {
            result.add(output.text)
        }
        return result
    }

    private fun parseInput(tree: Tree): LinkedList<String> {
        if (tree.type != WhileLexer.INPUTS) {
            throw RuntimeException("Expected INPUTS node")
        }
        val result = LinkedList<String>()
        val inputs = Compiler.getChildren(tree)
        for (input in inputs) {
            result.add(input.text)
        }
        return result
    }

    @JvmStatic
    fun toCode(result: MutableList<String>, tree: Tree) {
        val name = tree.getChild(0).text
        val inputs = tree.getChild(1)
        val inputsParsed = parseInput(inputs)
        val output = tree.getChild(3)
        val outputParsed = parseOutput(output)
        val body = tree.getChild(2)
        result.add("func begin $name")
        for (input in inputsParsed) {
            result.add("get $input")
        }
        Compiler.compile(body, result)
        for (s in outputParsed) {
            result.add("return $s")
        }
        result.add("Return")
        result.add("func end")
    }
}
