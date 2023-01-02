package org.example.translator;

import org.example.optimizer.NumberOfArgumentException;
import org.example.optimizer.OptimizeException;

import java.util.*;
import java.util.stream.Collectors;

public class Translator {

    List<String> res = new ArrayList<>();

    Set<String> variables = new HashSet<>();

    List<List<String>> words;
    int n;
    int tabs;

    final static String PARAMS_STACK = "params";
    final static String RETURN_STACK = "returnStack";


    public List<String> translate(List<String> strs) throws OptimizeException {
        words = strs.stream().map(line -> Arrays.stream(line.split(" ")).collect(Collectors.toList())).toList();
        n = 0;

        List<String> line;
        while((line = getNext()) != null) {
            String firstWord = line.get(0);

            switch (firstWord){
                case "func" -> func(line);
                case "return" -> ret(line);
                case "Return" -> Ret(line);
                case "if" -> If(line);
            }
            if(firstWord.startsWith("for_"))
                While();
            if(line.size() >= 2 && line.get(1).equals("="))
                assign(line);
            throw new OptimizeException(getLine(), "No operation found");
        }

        return res;
    }

    private void ret(List<String> line) throws NumberOfArgumentException {
        assertSize(line, 2);
        addLine("");
    }

    private void Ret(List<String> line) {
        addLine("return " + RETURN_STACK);
    }

    private void assertSize(List<String> line, int n) throws NumberOfArgumentException {
        if(line.size() < n)
            throw new NumberOfArgumentException(getLine());
    }

    private void func(List<String> line) throws OptimizeException {
        assertSize(line, 2);
        if(line.get(1).equals("begin")){
            assertSize(line, 3);
            int n = Integer.parseInt(line.get(2));

            addLine("function " + line.get(2) + "("+PARAMS_STACK+"){");
            tabs++;
            addLine("let " + RETURN_STACK + " = []");
        }else if(line.get(1).equals("end")){
            tabs--;
            addLine("}");
        }else{
            throw new OptimizeException(getLine(), "Operation not found");
        }
    }

    private String getLine(){
        return String.join(" ", words.get(n-1));
    }

    private void addLine(String line){
        res.add("\t".repeat(tabs) + line);
    }

    private List<String> getNext(){
        if(n >= words.size())
            return null;
        return words.get(n++);
    }
}
