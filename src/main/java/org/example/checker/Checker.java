package org.example.checker;

import org.antlr.runtime.tree.CommonTree;
import org.example.WhileLexer;
import org.example.checker.exception.*;
import org.example.checker.types.FunctionType;
import org.example.checker.types.Type;
import org.example.checker.types.VariableType;
import org.example.spaghetti.SpaghettiWrapper;
import org.example.spaghetti.exception.AlreadyExistException;
import org.example.spaghetti.exception.NotFoundException;
import org.example.spaghetti.exception.StackException;

import java.util.List;

public class Checker {
    CommonTree tree;
    String filename;

    public Checker(CommonTree tree, String filename) {
        this.tree = tree;
        this.filename = filename;
    }

    public void check() throws CheckerException {
        SpaghettiWrapper<Type> stack = new SpaghettiWrapper<>();
        symbolTable(stack, tree);
    }
    private void symbolTable(SpaghettiWrapper<Type> stack, CommonTree tree) throws CheckerException {
        switch (tree.getType()) {
            case WhileLexer.FUNCTION -> parseFunction(stack, tree);
            case WhileLexer.INPUTS -> parseInputs(stack, tree);
            case WhileLexer.OUTPUTS -> parseOutputs(stack, tree);
            case WhileLexer.VARS -> parseVars(stack, tree);
            case WhileLexer.Variable -> parseGetVars(stack, tree, VariableType.class);
            case WhileLexer.Symbol -> parseGetVars(stack, tree, FunctionType.class);
            case WhileLexer.COMMANDS -> {
                stack.down();
                for (Object child : tree.getChildren()) {
                    symbolTable(stack, (CommonTree) child);
                }
                stack.up();
            }
            default -> {
                if(tree.getChildren() == null)
                    return;
                for (Object child : tree.getChildren()) {
                    symbolTable(stack, (CommonTree) child);
                }
            }
        }
    }

    private void parseGetVars(SpaghettiWrapper<Type> stack, CommonTree tree, Class<?> typeClass) throws CheckerException {
        if(tree.getType() != WhileLexer.Variable && tree.getType() != WhileLexer.Symbol)
            return;

        String name = tree.getText();
        try {
            if(!stack.get(name).getClass().equals(typeClass)){
                throw new NotDeclaredException(filename, tree, name);
            }
        } catch (NotFoundException e) {
            throw new NotDeclaredException(filename, tree, name);
        } catch (StackException e) {
            throw new UnhandledException(filename, tree);
        }
    }

    private void parseVars(SpaghettiWrapper<Type> stack, CommonTree tree) throws CheckerException {
        for (Object child : tree.getChildren()) {
            String name = ((CommonTree)child).getText();

            try {
                if(stack.get(name) instanceof VariableType){

                }
            } catch (NotFoundException e) {
                try {
                    stack.newSet(name, new VariableType(name, tree.getLine()));
                } catch (StackException ex) {
                    throw new UnhandledException(filename, tree);
                }
            } catch (StackException e) {
                throw new UnhandledException(filename, tree);
            }
        }
    }

    private void parseInputs(SpaghettiWrapper<Type> stack, CommonTree tree) throws CheckerException {
        if(tree.getChildren() == null)
            return;
        for (Object child : tree.getChildren()) {
            CommonTree childTree = (CommonTree) child;
            String name = childTree.getText();

            try{
                stack.newSet(name, new VariableType(name, tree.getLine()));
            }catch (AlreadyExistException e){
                int line;
                try {
                    line = ((VariableType)stack.get(name)).getLine();
                } catch (StackException ex) {
                    throw new UnhandledException(filename, tree);
                }
                throw new VariableAlreadyExist(filename, tree, line);
            }catch (Exception e){
                throw new UnhandledException(filename, tree);
            }
        }
    }
    private void parseOutputs(SpaghettiWrapper<Type> stack, CommonTree tree) throws UnhandledException {
        for(Object child : tree.getChildren()){
            CommonTree childTree = (CommonTree) child;
            String name = childTree.getText();

            try {
                stack.newSet(name, new VariableType(name, tree.getLine()));
            } catch (StackException e) {
                throw new UnhandledException(filename, childTree);
            }
        }
    }

    private void parseFunction(SpaghettiWrapper<Type> stack, CommonTree tree) throws CheckerException {
        CommonTree name = (CommonTree) tree.getChild(0);
        CommonTree inputs = (CommonTree) tree.getChild(1);
        CommonTree commands = (CommonTree) tree.getChild(2);
        CommonTree outputs = (CommonTree) tree.getChild(3);

        try {
            stack.newSet(name.getText(), new FunctionType(name.getText(), tree.getLine()));
        } catch (AlreadyExistException e) {
            int line = 0;
            try {
                line = ((FunctionType)stack.get(name.getText())).getLine();
            } catch (StackException ex) {
                throw new UnhandledException(filename, tree);
            }
            throw new FunctionAlreadyExist(filename, tree, line);
        } catch (StackException e){
            throw new UnhandledException(filename, tree);
        }
        stack.down();
        symbolTable(stack, inputs);
        symbolTable(stack, commands);

        SpaghettiWrapper<Type> outputsStack = new SpaghettiWrapper<>();
        symbolTable(outputsStack, outputs);

        // Verify outputs;
        List<String> children = stack.listInChildren();
        for (String listInChild : outputsStack.listInChildren()) {
            if(!children.contains(listInChild))
                throw new NotDeclaredException(filename, outputs, listInChild);
        }
        stack.up();

    }
}
