package org.example.compiler

import org.antlr.runtime.tree.Tree

object Let {
    @JvmStatic
    fun toCode(result: MutableList<String>, tree: Tree) {
        val variables = tree.getChild(0)
        val expressionsTree = tree.getChild(1)
        val variablesNames = Array<String>(variables.childCount) { variables.getChild(it).text }
        val expressions = Array<Tree>(expressionsTree.childCount)  { expressionsTree.getChild(it) }
        for (i in variablesNames.indices) {
            val expression = Expression(expressions[i])
            val compose = expression.toCode()
            result.addAll(compose.prepend)
            result.add("${variablesNames[i]} = ${compose.value}")
        }
    }
}
