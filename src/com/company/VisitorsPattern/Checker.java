package com.company.VisitorsPattern;

import com.company.ast.Command;
import com.company.ast.CommandList;
import com.company.ast.Commands.*;
import com.company.ast.ExpressionList;
import com.company.ast.Commands.Expressions.*;
import com.company.ast.Program;
import com.company.ast.Terminals.Character;
import com.company.ast.Terminals.Identifier;
import com.company.ast.Terminals.Numbers;
import com.company.ast.Terminals.Operator;

import java.util.Vector;

public class Checker implements Visitor {
    private IdentificationTable idTable = new IdentificationTable();


    public void check(Program p) {
        p.visit(this, null);
    }


    public Object visitProgram(Program program, Object arg) {
        idTable.openScope();
        program.commandList.visit(this, null);
        idTable.closeScope();
        return null;
    }

    @Override
    public Object visitCommandList(CommandList commandList, Object arg) {
        for (Command command : commandList.commandList)
            command.visit(this, null);
        return null;
    }

    @Override
    public Object visitExpresionList(ExpressionList expressionList, Object arg) {
        for (Expression expression : expressionList.expressionList)
            expression.visit(this, null);
        return null;
    }

    @Override
    public Object visitArrayInst(ArrayInst arrayInst, Object arg) {
        arrayInst.name.visit(this, null);
        arrayInst.value.visit(this, null);
        return null;
    }

    @Override
    public Object visitCharInst(CharInst charInst, Object arg) {
        String id = (String) charInst.name.visit(this, null);
        idTable.enter(id, charInst);
        return null;
    }

    @Override
    public Object visitFunctionInst(FunctionInst functionInst, Object arg) {
        String id = (String) functionInst.name.visit(this, null);
        idTable.enter(id, functionInst);

        idTable.openScope();
        functionInst.args.visit(this, null);
        functionInst.body.visit(this, null);
        idTable.closeScope();

        return null;
    }

    @Override
    public Object visitIntInst(IntInst intInst, Object arg) {
        return null;
    }

    @Override
    public Object visitIfExec(IfExec ifExec, Object arg) {
        ifExec.condition.visit(this, null);
        ifExec.body.visit(this, null);

        return null;
    }

    @Override
    public Object visitPrintExec(PrintExec printExec, Object arg) {
        printExec.exp.visit(this, null);

        return null;
    }

    @Override
    public Object visitWhileExec(WhileExec whileExec, Object arg) {
        whileExec.condition.visit(this, null);
        whileExec.body.visit(this, null);

        return null;
    }

    public Object visitWhileStatement(WhileStatement w, Object arg) {

    }

    @Override
    public Object visitBinaryExpression(BinaryExpression binaryExpression, Object arg) {
        Type t1 = (Type) binaryExpression.operand1.visit(this, null);
        Type t2 = (Type) binaryExpression.operand2.visit(this, null);
        String operator = (String) binaryExpression.operator.visit(this, null);

//        if (operator.equals(":=") && t1.rvalueOnly)
//            System.out.println("Left-hand side of := must be a variable");

        return new Type(true);
    }

    @Override
    public Object visitCallExpression(CallExpression callExpression, Object arg) {
        String identifier = (String) callExpression.name.visit(this, null);
        Vector<Type> types = (Vector<Type>) (callExpression.args.visit(this, null));

        Command command = idTable.retrieve(identifier);
        if (command == null)
            System.out.println(identifier + " is not declared");
        else if (!(command instanceof FunctionInst))
            System.out.println(identifier + " is not a function");
        else {
            FunctionInst functionInst = (FunctionInst) command;
            callExpression.declaration = functionInst;

            if (functionInst.args.expressionList.size() != types.size())
                System.out.println("Incorrect number of arguments in call to " + identifier);
        }

        return new Type(true);
    }

    @Override
    public Object visitIdentifierExpression(IdentifierExpression identifierExpression, Object arg) {
        return null;
    }

    @Override
    public Object visitNumbersExpression(NumbersExpression numbersExpression, Object arg) {
        numbersExpression.numbers.visit(this, true);

        return new Type(true);
    }

    @Override
    public Object visitReturnExpression(ReturnExpression returnExpression, Object arg) {
        return null;
    }

    @Override
    public Object visitCharacter(Character character, Object arg) {
        return null;
    }

    @Override
    public Object visitIdentifier(Identifier identifier, Object arg) {
        return null;
    }

    @Override
    public Object visitNumbers(Numbers numbers, Object arg) {
        return null;
    }

    @Override
    public Object visitOperator(Operator operator, Object arg) {
        return null;
    }

    public Object visitExpressionStatement(ExpressionStatement e, Object arg) {
        e.exp.visit(this, null);

        return null;
    }

    public Object visitVarExpression(VarExpression v, Object arg) {
        String id = (String) v.name.visit(this, null);

        Declaration d = idTable.retrieve(id);
        if (d == null)
            System.out.println(id + " is not declared");
        else if (!(d instanceof VariableDeclaration))
            System.out.println(id + " is not a variable");
        else
            v.decl = (VariableDeclaration) d;

        return new Type(false);
    }


    public Object visitUnaryExpression(UnaryExpression u, Object arg) {
        u.operand.visit(this, null);
        String operator = (String) u.operator.visit(this, null);

        if (!operator.equals("+") && !operator.equals("-"))
            System.out.println("Only + or - is allowed as unary operator");

        return new Type(true);
    }


    public Object visitExpList(ExpList e, Object arg) {
        Vector<Type> types = new Vector<Type>();

        for (Expression exp : e.exp)
            types.add((Type) exp.visit(this, null));

        return types;
    }


    public Object visitIdentifier(Identifier i, Object arg) {
        return i.spelling;
    }


    public Object visitIntegerLiteral(IntegerLiteral i, Object arg) {
        return null;
    }


    public Object visitOperator(Operator o, Object arg) {
        return o.spelling;
    }
}
