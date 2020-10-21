package com.company.ast;

public class Program extends AST {
    public CommandList commandList;

    public Program(CommandList commandList) {
        this.commandList = commandList;
    }
}
