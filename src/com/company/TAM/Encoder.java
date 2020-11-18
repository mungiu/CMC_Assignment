package com.company.TAM;

import com.company.VisitorsPattern.Address;
import com.company.VisitorsPattern.Visitor;
import com.company.ast.Command;
import com.company.ast.CommandList;
import com.company.ast.Commands.*;
import com.company.ast.Commands.Expressions.*;
import com.company.ast.ExpressionList;
import com.company.ast.Program;
import com.company.ast.Terminals.Character;
import com.company.ast.Terminals.Identifier;
import com.company.ast.Terminals.Numbers;
import com.company.ast.Terminals.Operator;

import java.io.DataOutputStream;
import java.io.FileOutputStream;

public class Encoder implements Visitor {
    private int nextAdr = Machine.CB;
    private int currentLevel = 0;


    private void emit(int op, int n, int r, int d) {
        if (n > 255) {
            System.out.println("Operand too long");
            n = 255;
        }

        Instruction instr = new Instruction();
        instr.op = op;
        instr.n = n;
        instr.r = r;
        instr.d = d;

        if (nextAdr >= Machine.PB)
            System.out.println("Program too large");
        else
            Machine.code[nextAdr++] = instr;
    }

    private void patch(int adr, int d) {
        Machine.code[adr].d = d;
    }

    private int displayRegister(int currentLevel, int entityLevel) {
        if (entityLevel == 0)
            return Machine.SBr;
        else if (currentLevel - entityLevel <= 6)
            return Machine.LBr + currentLevel - entityLevel;
        else {
            System.out.println("Accessing across to many levels");
            return Machine.L6r;
        }
    }

    public void saveTargetProgram(String fileName) {
        try {
            DataOutputStream out = new DataOutputStream(new FileOutputStream(fileName));

            for (int i = Machine.CB; i < nextAdr; ++i)
                Machine.code[i].write(out);

            out.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Trouble writing " + fileName);
        }
    }

    public void encode(Program p) {
        p.visit(this, null);
    }

    @Override
    public Object visitProgram(Program p, Object arg) {
        currentLevel = 0;

        p.commandList.visit(this, new Address());
        emit(Machine.HALTop, 0, 0, 0);

        return null;
    }

    @Override
    public Object visitCommandList(CommandList commandList, Object arg) {
        int startDisplacement = ((Address) arg).displacement;

        for (Command command : commandList.commandList)
            arg = command.visit(this, arg);

        Address adr = (Address) arg;
        int size = adr.displacement - startDisplacement;

        return new Integer(size);
    }

    @Override
    public Object visitExpresionList(ExpressionList expressionList, Object arg) {
        int startDisplacement = ((Address) arg).displacement;

        for (Expression expression : expressionList.expressionList)
            arg = expression.visit(this, arg);

        Address adr = (Address) arg;
        int size = adr.displacement - startDisplacement;

        return new Integer(size);
    }

    @Override
    public Object visitArrayInst(ArrayInst arrayInst, Object arg) {
        String identifier = (String) arrayInst.name.visit(this, null);

        arrayInst.value.visit(this, null);
        return null;
    }

    @Override
    public Object visitCharInst(CharInst charInst, Object arg) {
        //DECLARATION
        charInst.adr = (Address) arg;
        Address adr = new Address((Address) arg, 1);

        //INSTANTIATION
        //boolean valueNeeded = ((Boolean) arg).booleanValue();
        //Address adr = v.decl.adr;
        int register = displayRegister(currentLevel, adr.level);

        //if (valueNeeded)
        emit(Machine.LOADop, 1, register, adr.displacement);

        return adr;
    }

    @Override
    public Object visitFunctionInst(FunctionInst functionInst, Object arg) {
        functionInst.adr = new Address(currentLevel, nextAdr);

        ++currentLevel;

        Address adr = new Address((Address) arg);

        int paramSize = ((Integer) functionInst.params.visit(this, adr)).intValue();
        functionInst.params.visit(this, new Address(adr, -paramSize));

        functionInst.body.visit(this, new Address(adr, Machine.linkDataSize));
        //functionInst.retExp.visit(this, new Boolean(true));

        emit(Machine.RETURNop, 1, 0, paramSize);

        currentLevel--;

        return arg;
    }

    @Override
    public Object visitIntInst(IntInst intInst, Object arg) {
        return null;
    }

    @Override
    public Object visitIfExec(IfExec ifExec, Object arg) {
        //pushing results onto the stack top
        ifExec.condition.visit(this, new Boolean(true));

        int jump1Adr = nextAdr; // saving local pointer to the address that comes next
        emit(Machine.JUMPIFop, 0, Machine.CBr, 0);   //NOTE: able to execute as soon as jump1Adr is patched

        //pushing results onto the stack top
        ifExec.body.visit(this, null);

        // int jump2Adr = nextAdr;
        //emit(Machine.JUMPop, 0, Machine.CBr, 0); NOT EXISTENT

        patch(jump1Adr, nextAdr); // updating the pointer with the actual discovered address

        //ifExec.elsePart.visit(this, null); NOT EXISTENT
        //patch(jump2Adr, nextAdr); NO-WHERE

        return null;
    }

    @Override
    public Object visitPrintExec(PrintExec printExec, Object arg) {
        return null;
    }

    @Override
    public Object visitWhileExec(WhileExec whileExec, Object arg) {
        int startAdr = nextAdr; // saving local pointer to the address that comes next

        whileExec.condition.visit(this, new Boolean(true));

        int jumpAdr = nextAdr;
        emit(Machine.JUMPIFop, 0, Machine.CBr, 0);  //NOTE: able to execute as soon as jumpAdr is patched

        whileExec.body.visit(this, null);

        emit(Machine.JUMPop, 0, Machine.CBr, startAdr); //NOTE: passing the the specific address
        patch(jumpAdr, nextAdr);

        return null;
    }

    @Override
    public Object visitBinaryExpression(BinaryExpression binaryExpression, Object arg) {
        return null;
    }

    @Override
    public Object visitCallExpression(CallExpression callExpression, Object arg) {
        return null;
    }

    @Override
    public Object visitIdentifierExpression(IdentifierExpression identifierExpression, Object arg) {
        return null;
    }

    @Override
    public Object visitNumbersExpression(NumbersExpression numbersExpression, Object arg) {
        return null;
    }

    @Override
    public Object visitReturnExpression(ReturnExpression returnExpression, Object arg) {
        return null;
    }

    @Override
    public Object visitUnaryExpression(UnaryExpression unaryExpression, Object arg) {
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
}
