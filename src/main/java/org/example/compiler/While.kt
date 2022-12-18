package org.example.compiler

import org.antlr.runtime.tree.Tree

/**
 * ┌─────┐
 * │While│
 * └┬─┬──┘
 *  │┌▽─────────┐
 *  ││Expression│
 *  │└──────────┘
 * ┌▽───────┐
 * │Commands│
 * └────────┘
 */
object While {
    fun toCode(result: List<String>, tree: Tree) {
        val expression = tree.getChild(0)
        val commands = tree.getChild(1)
        // generate code for expression
        val expressionClass = Expression(expression)
        val expressionResult = expressionClass.toCode()

    }
}
