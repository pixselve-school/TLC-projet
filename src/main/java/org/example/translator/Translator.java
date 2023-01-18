package org.example.translator;

import org.example.optimizer.NumberOfArgumentException;
import org.example.optimizer.OptimizeException;

import java.util.*;
import java.util.stream.Collectors;


/**
 * Used to translate 3addr to JS
 */
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


    /**
     * Transform to JS
     * @param strs lines of 3addr
     * @return lines of JS
     * @throws OptimizeException
     */
    public List<String> translate(List<String> strs) throws OptimizeException {
        words = strs.stream().map(line -> Arrays.stream(line.split(" ")).collect(Collectors.toList())).toList();
        n = 0;

        List<String> line;
        while((line = getNextInputLine()) != null) {
            translateLine(line);
        }

        return res;
    }

    /**
     * Translate a line
     * @param line list of words
     * @throws OptimizeException
     */
    private void translateLine(List<String> line) throws OptimizeException {
        String firstWord = line.get(0);

        switch (firstWord){
            case "func" -> translateFunction(line);
            case "return" -> addReturnValue(line);
            case "Return" -> translateReturn(line);
            case "if" -> parseIf(line, false);
            case "ifz" -> parseIf(line, true);
            case "param" -> addParam(line);
            case "call" -> translateCallFunction(line);
            case "goto" -> {}
            case "get" -> getParam(line);
            default -> {
                if(firstWord.startsWith("WHILE_"))
                    parseWhile(line);
                else if(firstWord.startsWith("FOR_"))
                    translateFor(line);
                else if(line.size() >= 2 && line.get(1).equals("="))
                    TranslateAssignation(line);
                else throw new OptimizeException(getCurrentLine(), "No operation found");
            }
        }
    }

    /**
     * Transform a for
     * @param line
     * @throws OptimizeException
     */
    private void translateFor(List<String> line) throws OptimizeException {
        variables.add(line.get(0));
        String init = getCurrentLine();
        String variable = line.get(0);
        getNextInputLine();
        line = getNextInputLine();
        getNextInputLine();

        String cond = "toInt(" + line.get(2) + ") " + line.get(3) + " " + line.get(4);

        String modif = line.get(2) + " = " + line.get(2) + "[1]";

        String lineFor = "for(" + init + "; !(" + cond + "); " + modif + "){";
        addLineToOutput(lineFor);
        tabs++;

        line = getNextInputLine();

        while(!line.get(0).equals(variable)){
            translateLine(line);

            line = getNextInputLine();
        }
        getNextInputLine();
        getNextInputLine();

        tabs--;
        addLineToOutput("}");
    }

    private void getParam(List<String> line) {
        addLineToOutput(line.get(1) + " = " + PARAMS_STACK_FUNC + ".shift()");
        variables.add(line.get(1));
    }

    private void translateCallFunction(List<String> line) throws NumberOfArgumentException {
        if(line.size() != 3)
            throw new NumberOfArgumentException(getCurrentLine());
        addLineToOutput(PREFIX+line.get(1) + "("+PARAMS_STACK+")");
        clearParam();
    }

    /**
     * Reset parameters of functions
     */
    private void clearParam(){
        addLineToOutput(PARAMS_STACK + " = []");
    }

    /**
     * Add a paramater for a function call
     * @param line
     */
    private void addParam(List<String> line) {
        String var = line.get(1);
        addLineToOutput(PARAMS_STACK + ".push(" + var + ")");
    }

    private void parseWhile(List<String> line) throws OptimizeException {
        String label = line.get(0);
        line = getNextInputLine();
        addLineToOutput("while(toBool("+line.get(1)+")){");
        tabs++;

        line = getNextInputLine();
        while(true){
            if(line.size() == 2 && line.get(0).equals("goto") && (line.get(1) + ":").equals(label)){
                getNextInputLine();
                break;
            }

            translateLine(line);
            line = getNextInputLine();
        }

        tabs--;
        addLineToOutput("}");
    }

    private void parseIf(List<String> line, boolean zero) throws OptimizeException {
        if(zero)
            addLineToOutput("if(!toBool("+line.get(1)+")){");
        else
            addLineToOutput("if(toBool("+line.get(1)+")){");

        tabs++;

        String label1 = line.get(3) + ":";
        String endLabel = "";

        List<String> lastLine = line;
        line = getNextInputLine();

        while(true){
            if(line.get(0).equals(label1)){
                if(lastLine.get(0).equals("goto")) {
                    endLabel = lastLine.get(1) + ":";
                    tabs--;
                    addLineToOutput("}else{");
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
            line = getNextInputLine();
        }

        tabs--;
        addLineToOutput("}");
    }

    private void TranslateAssignation(List<String> line) throws NumberOfArgumentException {
        if(line.size() < 3)
            throw new NumberOfArgumentException(getCurrentLine());

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
                addLineToOutput(leftName + " = []");
            }
        }
        addLineToOutput(line.get(0) + " = " + right);
        if(clearParams)
            clearParam();

    }

    private void addReturnValue(List<String> line) throws NumberOfArgumentException {
        assertSize(line, 2);
        addLineToOutput(RETURN_STACK + ".push(" +line.get(1)+ ")");
    }

    private void translateReturn(List<String> line) {
        addLineToOutput("return " + RETURN_STACK);
    }

    /**
     * Check if a line has enough words
     * @param line list of words
     * @param n number of minimum words
     * @throws NumberOfArgumentException
     */
    private void assertSize(List<String> line, int n) throws NumberOfArgumentException {
        if(line.size() < n)
            throw new NumberOfArgumentException(getCurrentLine());
    }

    private void translateFunction(List<String> line) throws OptimizeException {
        variables.clear();
        assertSize(line, 2);
        if(line.get(1).equals("begin")){
            assertSize(line, 3);

            addLineToOutput("function " + PREFIX+line.get(2) + "("+PARAMS_STACK_FUNC+"){");
            tabs++;
            addLineToOutput("let " + RETURN_STACK + " = []");
            addLineToOutput("let " + PARAMS_STACK + " = []");

            int lineDefine = getOutputLineIndex();

            line = getNextInputLine();
            while(line.size() != 2 || !line.get(0).equals("func") || !line.get(1).equals("end")){
                translateLine(line);
                line = getNextInputLine();
            }

            for(String var : variables){
                addLineToOutput("let " + var + " = null", lineDefine);
            }

            tabs--;
            addLineToOutput("}");
        }else{
            throw new OptimizeException(getCurrentLine(), "Operation not found");
        }
    }

    private String getCurrentLine(){
        return String.join(" ", words.get(n-1));
    }

    private void addLineToOutput(String line){
        res.add("\t".repeat(tabs) + line);
    }
    private void addLineToOutput(String line, int n){
        res.add(n, "\t".repeat(tabs) + line);
    }
    private int getOutputLineIndex(){
        return res.size();
    }

    private List<String> getNextInputLine(){
        if(n >= words.size())
            return null;
        return words.get(n++);
    }
}
