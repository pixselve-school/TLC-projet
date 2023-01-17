package org.example.compiler



import org.antlr.runtime.ANTLRStringStream
import org.antlr.runtime.CharStream
import org.antlr.runtime.CommonTokenStream
import org.antlr.runtime.RecognitionException
import org.antlr.runtime.tree.CommonTree
import org.antlr.runtime.tree.Tree
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.util.*
import org.example.WhileLexer
import org.example.WhileParser


object Compiler {
    var forCount = 0
    var ifCount = 0
    @JvmStatic
    fun compile(tree: Tree): List<String> {
        val result = mutableListOf<String>();
        compile(tree, result)
        return result
    }

    @JvmStatic
    fun reset() {
        forCount = 0
        ifCount = 0
        Expression.reset()
    }

    fun compile(tree: Tree, current: MutableList<String>) {
        val nodeType = tree.type
        when (nodeType) {
            WhileLexer.PROGRAM, WhileLexer.COMMANDS -> {
                val children = getChildren(tree)
                for (child in children) {
                    compile(child, current)
                }
            }
            WhileLexer.FUNCTION -> Function.toCode(current, tree)
            WhileLexer.IF -> If.toCode(current, tree, ifCount++)
            WhileLexer.LET -> Let.toCode(current, tree)
            WhileLexer.FOR -> For.toCode(current, tree, forCount++)
            WhileLexer.NOP -> {}
            else -> throw RuntimeException("Unknown node type: $nodeType")
        }
    }

    /**
     * Get children of a node.
     *
     * @param tree The node.
     * @return The children.
     */
    @JvmStatic
    fun getChildren(tree: Tree): ArrayList<Tree> {
        val children = ArrayList<Tree>()
        var i = 0
        while (true) {
            val child = tree.getChild(i) ?: break
            children.add(child)
            i++
        }
        return children
    }

    @Throws(IOException::class, RecognitionException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        val path = "src/main/resources/and.txt"
        val txt = Files.readString(Path.of(path))
        val cs: CharStream = ANTLRStringStream(txt)
        val lexer = WhileLexer(cs)
        val cts = CommonTokenStream(lexer)
        val parser = WhileParser(cts)
        val program = parser.program()
        val tree = program.tree as CommonTree
        val list = compile(tree)
        for (s in list) {
            println(s)
        }
    }
}
