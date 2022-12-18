package org.example.compiler

import org.antlr.runtime.tree.Tree

/**
 * ┌───────┐
 * │FOR    │
 * └┬─────┬┘
 * ┌▽───┐┌▽───────┐
 * │Opt1││COMMANDS│
 * └────┘└────────┘
 */
object For {
    @JvmStatic
    fun toCode(result: MutableList<String>, tree: Tree, index: Int) {
        val variable = tree.getChild(0)
        val commands = tree.getChild(1)
        result.add("for_label_$index:")
        result.add("if ${variable.text} goto end_for_label_$index")
        Compiler.compile(commands, result)
        result.add("goto for_label_$index")
        result.add("end_for_label_$index:")
    }
}
