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


    /**
     * Forms and instruction from given parameters and writes it into the next address
     *
     * @param OpCode
     * @param operandSize
     * @param registerNumber
     * @param operand
     */
    private void emit(int OpCode, int operandSize, int registerNumber, int operand) {
        if (operandSize > 255) {
            System.out.println("Operand too long");
            operandSize = 255;
        }

        Instruction instr = new Instruction();
        instr.OpCode = OpCode;
        instr.operandSize = operandSize;
        instr.registerNumber = registerNumber;
        instr.operand = operand;

        if (nextAdr >= Machine.PB)
            System.out.println("Program too large");
        else
            Machine.code[nextAdr++] = instr;
    }

    /**
     * Writes the discovered address into its placeholder
     *
     * @param adr     the placeholder for the address to be discovered
     * @param operand the discovered address
     */
    private void patch(int adr, int operand) {
        Machine.code[adr].operand = operand;
    }

    /**
     * Returns the register at which the entity is located???
     *
     * @param currentLevel
     * @param entityLevel
     * @return
     */
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

        if (commandList.commandList.size() > 0)
            emit(Machine.PUSHop, 0, 0, commandList.commandList.size());

        for (Command command : commandList.commandList)
            if (command != null)
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
        //TODO - not finalized
        String identifier = (String) arrayInst.name.visit(this, null);

        arrayInst.value.visit(this, null);
        return null;
    }

    @Override
    public Object visitCharInst(CharInst charInst, Object arg) {
        //DECLARATION
        // assigning given address to char instance
        Address passedInAdr = (Address) arg;
        charInst.adr = passedInAdr;
        // Address nextAdr = new Address((Address) arg, 1);

        //INSTANTIATION
        //boolean valueNeeded = ((Boolean) arg).booleanValue();
        //Address adr = v.decl.adr;
        // accessing the register of current charInst
        int register = displayRegister(currentLevel, charInst.adr.level);
        Character character = (Character) charInst.value.visit(this, null);

        //if (valueNeeded)
        // saving the current value at the address associated with the current char
        emit(Machine.STOREop, 1, register, charInst.adr.displacement);
        // emit(Machine.LOADop, 1, register, charInst.adr.displacement);


        return passedInAdr;
    }

    @Override
    public Object visitFunctionInst(FunctionInst functionInst, Object arg) {
        functionInst.adr = new Address(currentLevel, nextAdr);

        ++currentLevel;

        Address adr = new Address((Address) arg);

        // making space for parameters?
        int paramSize = ((Integer) functionInst.params.visit(this, adr)).intValue();
        functionInst.params.visit(this, new Address(adr, -paramSize));

        // executing the function body
        functionInst.body.visit(this, new Address(adr, Machine.linkDataSize));
        functionInst.returnCommand.visit(this, new Boolean(true));

        // this returns something, but what is method is void?
        emit(Machine.RETURNop, 1, 0, paramSize);

        currentLevel--;

        return arg;
    }

    @Override
    public Object visitIntInst(IntInst intInst, Object arg) {
        //DECLARATION
        // assigning given address to char instance
        Address passedInAdr = (Address) arg;
        intInst.adr = passedInAdr;
        // Address nextAdr = new Address((Address) arg, 1);

        //INSTANTIATION
        //boolean valueNeeded = ((Boolean) arg).booleanValue();
        //Address adr = v.decl.adr;
        // accessing the register of current charInst
        int register = displayRegister(currentLevel, intInst.adr.level);

        // NOTE: I pass the current intInst address so that the binary expression may know
        // where to store the end result
        Integer integer = (Integer) intInst.value.visit(this, intInst.adr);

        // storing the result of the binary expression in place of operand1
        emit(Machine.STOREop, 1, register, ((Address) arg).displacement);

//        //if (valueNeeded)
//        // loading the value from the stack onto the charInst
//        emit(Machine.LOADop, 1, register, intInst.adr.displacement);

        // incrementing the address so that the next command does not override the current
        return new Address((Address) arg, 1);
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
        // TODO ensure use
        printExec.exp.visit(this, new Boolean(true));

        emit(Machine.CALLop, 0, Machine.PBr, Machine.putintDisplacement);
        emit(Machine.CALLop, 0, Machine.PBr, Machine.puteolDisplacement);

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
        String operator = (String) binaryExpression.operator.visit(this, null);

        // NOTE: compared to the book, my first operand will never actually be a variable for storage
        binaryExpression.operand1.visit(this, arg);
        binaryExpression.operand2.visit(this, arg);

        if (operator.equals("+"))
            emit(Machine.CALLop, 0, Machine.PBr, Machine.addDisplacement);
        else if (operator.equals("-"))
            emit(Machine.CALLop, 0, Machine.PBr, Machine.subDisplacement);
        else if (operator.equals("*"))
            emit(Machine.CALLop, 0, Machine.PBr, Machine.multDisplacement);
        else if (operator.equals("/"))
            emit(Machine.CALLop, 0, Machine.PBr, Machine.divDisplacement);

//        int register = displayRegister(currentLevel, ((Address) arg).level);
//        // storing the result of the binary expression in place of operand1
//        emit(Machine.STOREop, 1, register, ((Address) arg).displacement);

        //// NOTE: We decided to NOT load the value into the register in here, since we can
        //// never be sure if the current binary expression is the last one i a possible chain
        //// instead, the final value is loaded inside the visitIntInst() method
        //// Loading the stored result onto the stack again?
        ////        emit(Machine.LOADop, 1, register, ((Address) arg).displacement);


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
//        boolean valueNeeded = ((Boolean) arg).booleanValue();

        Integer lit = (Integer) numbersExpression.numbers.visit(this, null);

//        if (valueNeeded)
        emit(Machine.LOADLop, 1, 0, lit.intValue());

        return null;
    }

    @Override
    public Object visitReturnExec(ReturnExec returnExec, Object arg) {
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
        return new Integer(numbers.spelling);
    }

    @Override
    public Object visitOperator(Operator operator, Object arg) {
        return operator.spelling;
    }
}
