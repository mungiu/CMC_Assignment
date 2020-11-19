package com.company;

import com.company.TAM.Encoder;
import com.company.VisitorsPattern.Checker;
import com.company.ast.Program;

import javax.swing.*;

public class TestDrivenCompiler {
    private static final String EXAMPLES_DIR = "C:\\Users\\andre\\OneDrive\\Documents\\Denmark University\\6th Semester\\CMC\\Assignments";


    public static void main(String args[]) {
        JFileChooser fc = new JFileChooser(EXAMPLES_DIR);

        if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            String sourceName = fc.getSelectedFile().getAbsolutePath();
            SourceFile in = new SourceFile(sourceName);
            // instantiating the scanner, and the parser.
            // while taking in the first found "word" in the document and creating the AST by parsing
            Scanner s = new Scanner(in);
            Parser p = new Parser(s);
            Program program = p.parseProgram();

            // instantiatinf the checker and the encoder
            // while the visitors pattern to visit the built AST
            Checker c = new Checker();
            Encoder e = new Encoder();

            c.check(program);
            e.encode(program);

            String targetName;
            if (sourceName.endsWith(".txt"))
                targetName = sourceName.substring(0, sourceName.length() - 4) + ".tam";
            else
                targetName = sourceName + ".tam";

            e.saveTargetProgram(targetName);
        }
    }
}
