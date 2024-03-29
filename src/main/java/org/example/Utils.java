package org.example;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class Utils {

    final static String LIBS_PATH = "src/main/resources/lib";
    final static String NUMBER_OF_MAIN_ARGS = "NUMBER_OF_MAIN_ARGS";

    public static Tree getTreeFromString(String txt) throws RecognitionException {
        CharStream cs = new ANTLRStringStream(txt);
        org.example.WhileLexer lexer = new org.example.WhileLexer(cs);
        CommonTokenStream cts = new CommonTokenStream(lexer);
        org.example.WhileParser parser = new org.example.WhileParser(cts);
        org.example.WhileParser.program_return program = parser.program();

        return (CommonTree) program.getTree();
    }

    public static List<String> getLibs(int numberOfMainArgs) throws IOException {
        List<String> res = new ArrayList<>();

        res.add("""
                /**
                 * ===============================================
                 * |                While STD lib                |
                 * ===============================================
                 */""");

        res.add("const " + NUMBER_OF_MAIN_ARGS + " = " + numberOfMainArgs + ";");

        File[] files = (new File(LIBS_PATH)).listFiles();

        for(File f : files){
            res.add("\n/**\n" +
                    " * ---------------- While STD Lib ----------------\n" +
                    " * " + f.getName() + "\n" +
                    " */\n");

            String all = Files.readString(f.toPath());
            res.addAll(List.of(all.split("\n")));
        }

        res.add("""
                /**
                 *  ===============================================
                 *  |                 End STD lib                 |
                 *  ===============================================
                 */""");

        return res;
    }
}
