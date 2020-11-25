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
import java.util.function.Function;

/**
 * Not this class has the task of ensuring that semantic rules are followed
 */
public class Checker implements Visitor {
    private IdentificationTable idTable = new IdentificationTable();


    public void check(Program program) {
        program.visit(this, null);
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
            if (command != null)
                command.visit(this, null);

        return null;
    }

    @Override
    public Object visitExpresionList(ExpressionList expressionList, Object arg) {
        for (Expression expression : expressionList.expressionList)
            if (expression != null)
                expression.visit(this, null);

        return null;
    }

    @Override
    public Object visitArrayInst(ArrayInst arrayInst, Object arg) {
        String identifier = (String) arrayInst.name.visit(this, null);
        idTable.enter(identifier, arrayInst);
        arrayInst.value.visit(this, null);
        return null;
    }

    @Override
    public Object visitCharInst(CharInst charInst, Object arg) {
        String identifier = (String) charInst.name.visit(this, null);
        charInst.value.visit(this, null);
        idTable.enter(identifier, charInst);
        return null;
    }

    @Override
    public Object visitFunctionInst(FunctionInst functionInst, Object arg) {
        String identifier = (String) functionInst.name.visit(this, null);
        idTable.enter(identifier, functionInst);

        idTable.openScope();
        functionInst.params.visit(this, null);
        functionInst.body.visit(this, null);
        functionInst.returnCommand.visit(this, null);
        idTable.closeScope();

        return null;
    }

    @Override
    public Object visitIntInst(IntInst intInst, Object arg) {
        String identifier = (String) intInst.name.visit(this, true);
        idTable.enter(identifier, intInst);
        intInst.value.visit(this, true);
        return null;
    }

    @Override
    public Object visitIfExec(IfExec ifExec, Object arg) {
        ifExec.condition.visit(this, null);

        idTable.openScope();
        ifExec.body.visit(this, null);
        idTable.closeScope();

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

        idTable.openScope();
        whileExec.body.visit(this, null);
        idTable.closeScope();

        return null;
    }

    @Override
    public Object visitBinaryExpression(BinaryExpression binaryExpression, Object arg) {
        Type t1 = (Type) binaryExpression.operand1.visit(this, null);
        Type t2 = (Type) binaryExpression.operand2.visit(this, null);

        String operator = (String) binaryExpression.operator.visit(this, null);

        if (!operator.equals("+")
                && !operator.equals("-")
                && !operator.equals("*")
                && !operator.equals("/")
                && !operator.equals("^"))
            System.out.println("A binary operator can only be + / - / * / '/' / ^");

        return new Type(true);
    }

    @Override
    public Object visitCallExpression(CallExpression callExpression, Object arg) {
        String identifier = (String) callExpression.name.visit(this, null);
        Vector<Type> types = null;
        if (callExpression.declaration instanceof FunctionInst)
            types = (Vector<Type>) (callExpression.args.visit(this, null));

        Command command = idTable.retrieve(identifier);
        if (command == null)
            System.out.println(identifier + " is not declared");
        else if (!(command instanceof FunctionInst)
                && !(command instanceof IntInst)
                && !(command instanceof CharInst)
                && !(command instanceof ArrayInst))
            System.out.println(identifier + " is not a call of an instance");
        else {
            callExpression.declaration = command;
            if (command instanceof FunctionInst
                    && ((FunctionInst) command).params.expressionList.size() != types.size())
                System.out.println("Incorrect number of arguments in call to " + identifier);
        }

        return new Type(true);
    }

    @Override
    public Object visitIdentifierExpression(IdentifierExpression identifierExpression, Object arg) {
        identifierExpression.identifier.visit(this, true);

        return new Type(true);
    }

    @Override
    public Object visitNumbersExpression(NumbersExpression numbersExpression, Object arg) {
        numbersExpression.numbers.visit(this, true);

        return new Type(true);
    }

    public Object visitUnaryExpression(UnaryExpression unaryExpression, Object arg) {
        String operator = (String) unaryExpression.operator.visit(this, null);
        unaryExpression.operand.visit(this, null);

        if (!operator.equals("++") && !operator.equals("--"))
            System.out.println("Only ++ or -- is allowed as unary operator");

        return new Type(true);
    }

    @Override
    public Object visitCharacter(Character character, Object arg) {
        return character.aChar;
    }

    @Override
    public Object visitIdentifier(Identifier identifier, Object arg) {
        return identifier.spelling;
    }

    @Override
    public Object visitNumbers(Numbers numbers, Object arg) {
        return null;
    }

    @Override
    public Object visitOperator(Operator operator, Object arg) {
        return operator.spelling;
    }
}
