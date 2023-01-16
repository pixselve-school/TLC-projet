package org.example.prettyPrintAST

import org.antlr.runtime.tree.Tree

fun prettyPrint(tree: Tree): Unit {
    val list = mutableListOf<String>()
    list.add("flowchart")
    prettyPrint(tree, list, 0)
    for (line in list) {
        println(line)
    }
}

fun prettyPrint(tree: Tree, list: MutableList<String>, index: Int): MutableList<String> {
    val childCount = tree.childCount
    if (childCount == 0) {
        list.add("$index[${tree.text}]")
    } else {
        for (i in 0 until childCount) {
            val child = tree.getChild(i)
            val childIndex = list.size
            list.add("$index[${tree.text}] --> $childIndex")
            prettyPrint(child, list, childIndex)
        }
    }
    return list
}
