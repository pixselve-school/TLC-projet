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

    final static String PARAMS_STACK = "paramsStack";
    final static String PARAMS_STACK_FUNC = "paramsStackFunc";
    final static String RETURN_STACK = "returnStack";
    final static String PREFIX = "tlc_";


    public List<String> translate(List<String> strs) throws OptimizeException {
        words = strs.stream().map(line -> Arrays.stream(line.split(" ")).collect(Collectors.toList())).toList();
        n = 0;

        List<String> line;
        while((line = getNext()) != null) {
            translateLine(line);
        }

        return res;
    }

    private void translateLine(List<String> line) throws OptimizeException {
        String firstWord = line.get(0);

        switch (firstWord){
            case "func" -> func(line);
            case "return" -> ret(line);
            case "Return" -> Ret(line);
            case "if" -> If(line);
            case "param" -> param(line);
            case "call" -> call(line);
            case "goto" -> {}
            case "get" -> get(line);
            default -> {
                if(firstWord.startsWith("for_"))
                    While(line);
                else if(firstWord.startsWith("FOR_"))
                    For(line);
                else if(line.size() >= 2 && line.get(1).equals("="))
                    assign(line);
                else throw new OptimizeException(getLine(), "No operation found");
            }
        }
    }

    private void For(List<String> line) throws OptimizeException {
        variables.add(line.get(0));
        String init = getLine();
        String variable = line.get(0);
        getNext();
        line = getNext();
        getNext();

        String cond = "toInt(" + line.get(2) + ") " + line.get(3) + " " + line.get(4);

        String modif = line.get(2) + " = " + line.get(2) + "[1]";

        String lineFor = "for(" + init + "; !(" + cond + "); " + modif + "){";
        addLine(lineFor);
        tabs++;

        line = getNext();

        while(!line.get(0).equals(variable)){
            translateLine(line);

            line = getNext();
        }
        getNext();
        getNext();

        tabs--;
        addLine("}");
    }

    private void get(List<String> line) {
        addLine(line.get(1) + " = " + PARAMS_STACK_FUNC + ".shift()");
        variables.add(line.get(1));
    }

    private void call(List<String> line) throws NumberOfArgumentException {
        if(line.size() != 3)
            throw new NumberOfArgumentException(getLine());
        addLine(PREFIX+line.get(1) + "("+PARAMS_STACK+")");
        clearParam();
    }

    private void clearParam(){
        addLine(PARAMS_STACK + " = []");
    }

    private void param(List<String> line) {
        String var = line.get(1);
        addLine(PARAMS_STACK + ".push(" + var + ")");
    }

    private void While(List<String> line) throws OptimizeException {
        String label = line.get(0);
        line = getNext();
        addLine("while(!toBool("+line.get(1)+")){");
        tabs++;

        line = getNext();
        while(true){
            if(line.size() == 2 && line.get(0).equals("goto") && (line.get(1) + ":").equals(label)){
                getNext();
                break;
            }

            translateLine(line);
            line = getNext();
        }

        tabs--;
        addLine("}");
    }

    private void If(List<String> line) throws OptimizeException {
        addLine("if(toBool("+line.get(1)+")){");

        tabs++;

        String label1 = line.get(3) + ":";
        String endLabel = "";

        List<String> lastLine = line;
        line = getNext();

        while(true){
            if(line.get(0).equals(label1)){
                if(lastLine.get(0).equals("goto")) {
                    endLabel = lastLine.get(1) + ":";
                    tabs--;
                    addLine("}else{");
                    tabs++;
                }else{
                    break;
                }
            }else if(line.get(0).equals(endLabel)){
                break;
            } else {
                translateLine(line);
            }
            lastLine = line;
            line = getNext();
        }

        tabs--;
        addLine("}");
    }

    private void assign(List<String> line) throws NumberOfArgumentException {
        if(line.size() < 3)
            throw new NumberOfArgumentException(getLine());

        String leftName = line.get(0);
        String right = line.get(2);

        if(right.equals("nil"))
            right = "null";

        boolean clearParams = false;

        if(line.size() == 5){
            right = PREFIX+line.get(3) + "("+PARAMS_STACK+")";
            clearParams = true;
        }


        int bracLeft = leftName.indexOf("[");
        int n = -1;
        if(bracLeft != -1){
            n = Integer.parseInt(
                    leftName.substring(bracLeft+1, leftName.length()-1)
            );
            leftName = leftName.substring(0, bracLeft);
        }

        if(!variables.contains(leftName)){
            variables.add(leftName);
            if(n != -1){
                addLine(leftName + " = []");
            }
        }
        addLine(line.get(0) + " = " + right);
        if(clearParams)
            clearParam();

    }

    private void ret(List<String> line) throws NumberOfArgumentException {
        assertSize(line, 2);
        addLine(RETURN_STACK + ".push(" +line.get(1)+ ")");
    }

    private void Ret(List<String> line) {
        addLine("return " + RETURN_STACK);
    }

    private void assertSize(List<String> line, int n) throws NumberOfArgumentException {
        if(line.size() < n)
            throw new NumberOfArgumentException(getLine());
    }

    private void func(List<String> line) throws OptimizeException {
        variables.clear();
        assertSize(line, 2);
        if(line.get(1).equals("begin")){
            assertSize(line, 3);

            addLine("function " + PREFIX+line.get(2) + "("+PARAMS_STACK_FUNC+"){");
            tabs++;
            addLine("let " + RETURN_STACK + " = []");
            addLine("let " + PARAMS_STACK + " = []");

            int lineDefine = getIndex();

            line = getNext();
            while(line.size() != 2 || !line.get(0).equals("func") || !line.get(1).equals("end")){
                translateLine(line);
                line = getNext();
            }

            for(String var : variables){
                addLine("let " + var + " = null", lineDefine);
            }

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
    private void addLine(String line, int n){
        res.add(n, "\t".repeat(tabs) + line);
    }
    private int getIndex(){
        return res.size();
    }

    private List<String> getNext(){
        if(n >= words.size())
            return null;
        return words.get(n++);
    }
}
