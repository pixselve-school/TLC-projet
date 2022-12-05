package org.example.checker.exception;

import org.antlr.runtime.tree.CommonTree;

public abstract class CheckerException extends Exception {

    String filename;
    int line;
    int charPos;
    String message;

    public CheckerException(String filename, CommonTree tree, String message){
        super("\n" +
                "\t" + filename + ":" + tree.getLine() + ":" + tree.getCharPositionInLine() + "\n" +
                "\t" + message + "\n"
        );
        this.filename = filename;
        line = tree.getLine();
        charPos = tree.getCharPositionInLine();
        this.message = message;
    }

    public String toString(String line){
        StringBuilder res = new StringBuilder();

        String spaces = "-".repeat(charPos) + '^';

        res.append(message);
        res.append('\n');
        res.append("\tat ").append(filename).append(':').append(this.line).append(':').append(charPos).append('\n');
        res.append('\t').append(line).append('\n');
        res.append('\t').append(spaces);
        res.append("\n\n");

        return res.toString();
    }

    public String getFilename() {
        return filename;
    }

    public int getLine() {
        return line;
    }

    public int getCharPos() {
        return charPos;
    }
}
