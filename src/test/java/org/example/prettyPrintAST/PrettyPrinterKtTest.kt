package org.example.prettyPrintAST

import org.example.Utils
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class PrettyPrinterKtTest {
    @Test
    fun `main test`() {
        val code = """
          if Opt1 then
          nop
          fi
        
          od
        """.trimIndent()
        val tree = Utils.getTreeFromString("function test: read A % $code % write B")
        prettyPrint(tree)

    }
}
