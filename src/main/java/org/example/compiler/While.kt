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

    fun toCode(result: MutableList<String>, tree: Tree, index: Int) {
        val expression = tree.getChild(0)
        val commands = tree.getChild(1)
        // generate code for expression
        val expressionClass = Expression(expression)
        val expressionResult = expressionClass.toCode()
        // generate code for commands
        val codeInsideCommands = Compiler.compile(commands)
        // generate code for while
        result.addAll(expressionResult.prepend)
        result.add("WHILE_BEFORE_COND_$index:")
        // if expression is false, jump to the end of while
        result.add("JZ WHILE_AFTER_WHILE_$index")
        // if expression is true, execute commands
        result.addAll(codeInsideCommands)
        // jump to the beginning of while
        result.add("JMP WHILE_BEFORE_COND_$index")
        result.add("WHILE_AFTER_WHILE_$index:")
    }
}
