package com.company.VisitorsPattern;

import com.company.ast.CommandList;
import com.company.ast.Commands.*;
import com.company.ast.ExpressionList;
import com.company.ast.Commands.Expressions.*;
import com.company.ast.Program;
import com.company.ast.Terminals.*;
import com.company.ast.Terminals.Character;

public interface Visitor {
    public Object visitProgram(Program p, Object arg);

    public Object visitCommandList(CommandList commandList, Object arg);

    public Object visitExpresionList(ExpressionList expressionList, Object arg);


    // COMMANDS
    public Object visitArrayInst(ArrayInst arrayInst, Object arg);

    public Object visitCharInst(CharInst charInst, Object arg);

    public Object visitFunctionInst(FunctionInst functionInst, Object arg);

    public Object visitIntInst(IntInst intInst, Object arg);

    public Object visitIfExec(IfExec ifExec, Object arg);

    public Object visitPrintExec(PrintExec printExec, Object arg);

    public Object visitWhileExec(WhileExec whileExec, Object arg);


    // EXPRESSIONS
    public Object visitBinaryExpression(BinaryExpression binaryExpression, Object arg);

    public Object visitCallExpression(CallExpression callExpression, Object arg);

    public Object visitIdentifierExpression(IdentifierExpression identifierExpression, Object arg);

    public Object visitNumbersExpression(NumbersExpression numbersExpression, Object arg);

    public Object visitReturnExpression(ReturnExpression returnExpression, Object arg);

    public Object visitUnaryExpression(UnaryExpression unaryExpression, Object arg);


    // TERMINALS
    public Object visitCharacter(Character character, Object arg);

    public Object visitIdentifier(Identifier identifier, Object arg);

    public Object visitNumbers(Numbers numbers, Object arg);

    public Object visitOperator(Operator operator, Object arg);
}
