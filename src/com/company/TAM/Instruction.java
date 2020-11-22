package com.company.TAM;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;

public class Instruction {
    public Instruction() {
        OpCode = 0;
        length = 0;
        registerNumber = 0;
        operand = 0;
    }

    // Java has no type synonyms, so the following representations are
    // assumed:
    //
    //  type
    //    OpCode = 0..15;  {4 bits unsigned}
    //    Length = 0..255;  {8 bits unsigned}
    //    Operand = -32767..+32767;  {16 bits signed}

    // Represents TAM instructions.
    public int OpCode; // OpCode
    public int length;  // RegisterNumber
    public int registerNumber;  // Length
    public int operand;  // Operand

    public void write(DataOutputStream output) throws IOException {
        output.writeInt(OpCode);
        output.writeInt(length);
        output.writeInt(registerNumber);
        output.writeInt(operand);
    }

    public static Instruction read(DataInputStream input) throws IOException {
        Instruction inst = new Instruction();
        try {
            inst.OpCode = input.readInt();
            inst.length = input.readInt();
            inst.registerNumber = input.readInt();
            inst.operand = input.readInt();
            return inst;
        } catch (EOFException s) {
            return null;
        }
    }
}
