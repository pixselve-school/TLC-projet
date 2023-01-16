package org.example.compiler

import org.antlr.runtime.tree.Tree

/**
 * ┌───────┐
 * │FOR    │
 * └┬─────┬┘
 * ┌▽───┐┌▽───────┐
 * │Opt1││COMMANDS│
 * └────┘└────────┘
 *
 * Attention : l’exécution de C ne doit pas perturber le décompte du for.
 * Il faut donc sauvegarder le compteur du for dans une variable temporaire.
 */
object For {
    @JvmStatic
    fun toCode(result: MutableList<String>, tree: Tree, index: Int) {
        val variable = tree.getChild(0)
        val commands = tree.getChild(1)
        // generate code for variable
        val variableClass = Expression(variable)
        val variableResult = variableClass.toCode()
        result.addAll(variableResult.prepend)
        // make a copy of the variable
        result.add("FOR_VARIABLE_$index = ${variableResult.value}")
        // generate code for commands
        val codeInsideCommands = Compiler.compile(commands)
        // generate code for for
        result.add("FOR_BEFORE_COND_$index:")
        result.add("FOR_VARIABLE_COND_$index = FOR_VARIABLE_$index <= 0")
        // if variable is false, jump to the end of for
        result.add("if FOR_VARIABLE_COND_$index goto FOR_AFTER_FOR_$index")
        // if variable is true, execute commands
        result.addAll(codeInsideCommands)
        // decrement variable
        result.add("FOR_VARIABLE_$index = FOR_VARIABLE_$index[1]")
        // jump to the beginning of for
        result.add("goto FOR_BEFORE_COND_$index")
        result.add("FOR_AFTER_FOR_$index:")


    }
}
