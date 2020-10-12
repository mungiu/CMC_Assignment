package com.company;

import javax.swing.*;

public class TestDriverParser {
    private static final String EXAMPLES_DIR = "C:\\Users\\andre\\OneDrive\\Documents\\Denmark University\\6th Semester\\CMC\\Assignments";


    public static void main( String args[] )
    {
        JFileChooser fc = new JFileChooser( EXAMPLES_DIR );

        if( fc.showOpenDialog( null ) == JFileChooser.APPROVE_OPTION ) {
            SourceFile in = new SourceFile( fc.getSelectedFile().getAbsolutePath() );
            // instantiating the scanned and the parser and taking in the first found "word" in teh document
            Scanner s = new Scanner( in );
            Parser p = new Parser( s );

            p.parseProgram();
        }
    }
}
