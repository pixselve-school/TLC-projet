package org.example.checker;

import org.antlr.runtime.tree.CommonTree;
import org.example.WhileLexer;
import org.example.checker.exception.CheckerException;
import org.example.checker.exception.FunctionAlreadyExist;
import org.example.checker.exception.UnhandledException;
import org.example.checker.exception.VariableAlreadyExist;
import org.example.checker.types.FunctionType;
import org.example.checker.types.Type;
import org.example.checker.types.VariableType;
import org.example.spaghetti.SpaghettiWrapper;
import org.example.spaghetti.exception.AlreadyExistException;
import org.example.spaghetti.exception.StackException;

import javax.management.InstanceNotFoundException;

public class Checker {
    CommonTree tree;

    public Checker(CommonTree tree) {
        this.tree = tree;
    }

    public void check(){

    }
    public void symbolTable(SpaghettiWrapper<Type> stack, CommonTree tree) throws CheckerException {
        try {
            switch (tree.getType()) {
                case WhileLexer.FUNCTION -> parseFunction(stack, tree);
                case WhileLexer.INPUTS -> parseInputs(stack, tree);
                default -> {
                    for (Object child : tree.getChildren()) {
                        symbolTable(stack, (CommonTree) child);
                    }
                }
            }
        } catch (Exception e){

        }
    }

    private void parseInputs(SpaghettiWrapper<Type> stack, CommonTree tree) throws StackException, CheckerException {
        for (Object child : tree.getChildren()) {
            CommonTree childTree = (CommonTree) child;
            String name = childTree.getText();

            try{
                stack.newSet(name, new VariableType(name, tree.getLine()));
            }catch (AlreadyExistException e){
                int line = ((VariableType)stack.get(name)).getLine();
                throw new VariableAlreadyExist(tree, line);
            }catch (Exception e){
                throw new UnhandledException(tree);
            }
        }
    }

    private void parseFunction(SpaghettiWrapper<Type> stack, CommonTree tree) throws StackException, CheckerException {
        CommonTree name = (CommonTree) tree.getChild(0);
        CommonTree inputs = (CommonTree) tree.getChild(1);
        CommonTree commands = (CommonTree) tree.getChild(2);
        CommonTree outputs = (CommonTree) tree.getChild(3);

        try {
            stack.newSet(name.getText(), new FunctionType(name.getText(), tree.getLine()));
        } catch (AlreadyExistException e) {
            int line = ((FunctionType)stack.get(name.getText())).getLine();
            throw new FunctionAlreadyExist(tree, line);
        }
        symbolTable(stack, inputs);
        stack.down();
        symbolTable(stack, commands);
        stack.up();
        symbolTable(stack, outputs);
    }
}
