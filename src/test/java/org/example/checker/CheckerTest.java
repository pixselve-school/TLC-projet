package org.example.checker;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;
import org.example.checker.exception.CheckerException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CheckerTest {

    String main = "" +
            "function main :\n" +
            "   read\n" +
            "   %\n" +
            "       Result := (cons)\n" +
            "   %\n" +
            "   write Result\n\n";

    boolean check(String s) throws RecognitionException, CheckerException {
        CharStream cs = new ANTLRStringStream(main+s);
        org.example.WhileLexer lexer = new org.example.WhileLexer(cs);
        CommonTokenStream cts = new CommonTokenStream(lexer);
        org.example.WhileParser parser = new org.example.WhileParser(cts);
        org.example.WhileParser.program_return program = parser.program();

        CommonTree tree = (CommonTree) program.getTree();
        Checker checker = new Checker(tree, "test", main+s);
        return checker.check();
    }


    @Test
    void checkFunctionExist() throws CheckerException, RecognitionException {
        String f1 =
                "function true :\n" +
                "    read\n" +
                "    %\n" +
                "        Result := (cons nil nil)\n" +
                "    %\n" +
                "    write Result\n";

        assertTrue(check(f1));

        String f2 =
                "function false :\n" +
                        "    read\n" +
                        "    %\n" +
                        "        Result := (true)\n" +
                        "    %\n" +
                        "    write Result\n";
        assertFalse(check(f2));

        String f3 = f1 + f2;
        assertTrue(check(f3));
    }

    @Test
    void checkVariableExist() throws CheckerException, RecognitionException {
        String f1 =
                "function true :\n" +
                "    read\n" +
                "    %\n" +
                "        Result := (cons nil nil)\n" +
                "    %\n" +
                "    write Result\n";
        String f2 =
                "function true :\n" +
                        "    read\n" +
                        "    %\n" +
                        "        Result := (cons nil nil)\n" +
                        "    %\n" +
                        "    write Result1\n";

        assertTrue(check(f1));
        assertFalse(check(f2));
    }

    @Test
    void checkAlreadyExist() throws CheckerException, RecognitionException {
        String f1 =
                "function true :\n" +
                "    read A, B, C\n" +
                "    %\n" +
                "        Result := (cons nil nil)\n" +
                "    %\n" +
                "    write Result\n";
        String f2 =
                "function true :\n" +
                        "    read A, B, A\n" +
                        "    %\n" +
                        "        Result := (cons nil nil)\n" +
                        "    %\n" +
                        "    write Result\n";

        assertTrue(check(f1));
        assertFalse(check(f2));
    }
}