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

    fun toCode(result: MutableList<String>, tree: Tree, index: String) {
        val expression = tree.getChild(0)
        val commands = tree.getChild(1)
        // generate code for expression
        val expressionClass = Expression(expression)
        val expressionResult = expressionClass.toCode()
        // generate code for commands
        val codeInsideCommands = Compiler.compile(commands)
        // generate code for while
        result.addAll(expressionResult.prepend)
        result.add("param ${expressionResult.value}")
        result.add("WHILE_BOOL_$index = call convertToBoolean 1")
        result.add("WHILE_$index:")
        result.addAll(codeInsideCommands)
        result.add("param ${expressionResult.value}")
        result.add("WHILE_BOOL_$index = call convertToBoolean 1")
        result.add("if WHILE_BOOL_$index goto WHILE_$index")
    }
}
