package org.example.compiler

import org.antlr.runtime.tree.Tree


object If : Element {
    @JvmStatic
    fun toCode(result: MutableList<String>, tree: Tree, index: Int) {
        val condition = tree.getChild(0)
        val thenBranch = tree.getChild(1)
        val elseBranch = tree.getChild(2)
        val conditionExp = Expression(condition)
        val compose = conditionExp.toCode()
        result.addAll(compose.prepend)
        result.add("if ${compose.value} goto false_label_$index")
        Compiler.compile(thenBranch, result)
        if (elseBranch == null) {
            result.add("false_label_$index:")
        } else {
            result.add("goto end_label_$index")
            result.add("false_label_$index:")
            Compiler.compile(elseBranch, result)
            result.add("end_label_$index:")
        }
    }
}
