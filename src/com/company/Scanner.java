/*
 * 23.08.2019 TokenKind enum introduced
 * 16.08.2016 IScanner gone, minor editing
 * 20.09.2010 IScanner
 * 25.09.2009 New package structure
 * 22.09.2006 Original version (based on Example 4.21 in Watt&Brown)
 */
 
package com.company;


import static com.company.TokenKind.*;

// WORKED ON BY JEPPE
public class Scanner
{
	private SourceFile source;
	
	private char currentChar;
	private StringBuffer currentSpelling = new StringBuffer();
	
	
	public Scanner( SourceFile source )
	{
		this.source = source;
		
		currentChar = source.getSource();
	}

	/**
	 * Appends the current character to currentSpelling and reads the next character from source imediatelly
	 */
	private void takeIt()
	{
		currentSpelling.append( currentChar );
		currentChar = source.getSource();
	}
	
	
	private boolean isLetter( char c )
	{
		return c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z';
	}
	
	
	private boolean isDigit( char c )
	{
		return c >= '0' && c <= '9';
	}
	
	
	private void scanSeparator()
	{
		switch (currentChar) {
			case '#':
				takeIt();
				while (currentChar != SourceFile.EOL && currentChar != SourceFile.EOT)
					takeIt();

				if (currentChar == SourceFile.EOL)
					takeIt();
				break;
			case ' ':
			case '\n':  //	\n matches linefeed.
			case '\r':  //	\r matches carriage return.
			case '\t':  //	\t matches horizontal tab.
				takeIt();
				break;
		}
	}
	

	private TokenKind scanToken()
	{
		if( isLetter( currentChar ) ) {
			takeIt();
			while( isLetter( currentChar ) || isDigit( currentChar ) )
				takeIt();
				
			return IDENTIFIER;
			
		} else if( isDigit( currentChar ) ) {
			takeIt();
			while( isDigit( currentChar ) )
				takeIt();
				
			return NUMBERS;
			
		} switch( currentChar ) {
			case '^': case '!': case '*': case '/':
				takeIt();

				return OPERATOR;

			case '+': case '-':
				takeIt();
				//must use if statement, switch case only for constants it seems.
				//to check for ++ and --. If next character is same as previous character
				if(currentChar == currentSpelling.charAt(currentSpelling.length()-1))
					takeIt();

				return OPERATOR;

			case '\'':
				takeIt();
				return APOSTROPHE;
				
			case ',':
				takeIt();
				return COMMA;
				
			case ';':
				takeIt();
				return SEMICOLON;
				
			case '(':
				takeIt();
				return LEFT_PARANTHESIS;
				
			case ')':
				takeIt();
				return RIGHT_PARANTHESIS;

			case '[':
				takeIt();
				return LEFT_SQUARE_BRACKET;

			case ']':
				takeIt();
				return RIGHT_SQUARE_BRACKET;

			case '=':
				takeIt();
				return EQUALSIGN;

			case '&':
				takeIt();
				return WHILE;

				
			case SourceFile.EOT:
				return EOT;
				
			default:
				takeIt();
				return ERROR;
		}
	}
	
	
	public Token scan()
	{
		while( currentChar == '#' || currentChar == '\n' ||
		       currentChar == '\r' || currentChar == '\t' ||
		       currentChar == ' ' )
			scanSeparator();

		// currentSpelling is filled up by scanToken();
		currentSpelling = new StringBuffer( "" );
		TokenKind kind = scanToken();

		// currently the token is generic and the specific type will be decided inside the constructor FUCK UGLY
		return new Token( kind, new String( currentSpelling ) );
	}
}
