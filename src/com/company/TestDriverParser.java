package com.company;

import com.company.ast.Program;

import javax.swing.*;

public class TestDriverParser {
    private static final String EXAMPLES_DIR = "C:\\Users\\andre\\OneDrive\\Documents\\Denmark University\\6th Semester\\CMC\\Assignments";


    public static void main(String args[]) {
        JFileChooser fc = new JFileChooser(EXAMPLES_DIR);

        if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            SourceFile in = new SourceFile(fc.getSelectedFile().getAbsolutePath());
            // instantiating the scanner, and the parser.
            // while taking in the first found "word" in the document and creating the AST by parsing
            Scanner s = new Scanner(in);
            Parser p = new Parser(s);
            Program program = p.parseProgram();
        }
    }
}
