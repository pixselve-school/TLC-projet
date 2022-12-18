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

    @JvmStatic
    fun toCode(result: MutableList<String>, tree: Tree) {
        val name = tree.getChild(0).text
        val output = tree.getChild(3)
        val outputParsed = parseOutput(output)
        val body = tree.getChild(2)
        result.add("func begin $name")
        Compiler.compile(body, result)
        for (s in outputParsed) {
            result.add("return $s")
        }
        result.add("Return")
        result.add("func end")
    }
}
