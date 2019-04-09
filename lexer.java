package lexer;
import java.io.*;

//=============================================//
//=============================================//
public class Token {
	private int token;
	private String lexeme;

	public Token(int token, String lexeme) {
		this.token = token;
		this.lexeme = lexeme;
	}

	// Returns the type of the token
	public int getTokenType() {
		return token;
	}

	// Returns the lexeme of the token
	public String getLexeme() {
		return lexeme;
	}

	public String toString() {
		return "Next token is: " + token + ", Next lexeme is " + lexeme;
	}
}
//=============================================//
//=============================================//

//=============================================//
public class Lexer {
//=============================================//
//function gets next character of input from the
//input program and puts it in the global variab
//le. function also determines the character cla
//ss of the input character and puts in the glob
//al character class.                 	       
//=============================================//
	private BufferedReader reader;
	private char c;
//=============================================//
	private static final char EOF = (char) (-1);
    private static final int LETTER = 0;
    private static final int DIGIT = 1;
    private static final int UNKNOWN = 99;
    private static final int INT_LIT = 10;
    private static final int IDENT = 11;
    private static final int ASSIGN_OP = 20;
    private static final int ADD_OP = 21;
    private static final int SUB_OP = 22;
    private static final int MULT_OP = 23;
    private static final int DIV_OP = 24;
    private static final int LEFT_PAREN = 25;
    private static final int RIGHT_PAREN = 26;
    private static final int ERROR = 27;
//=============================================//
    public Lexer(String file) {
    	try {
         	reader = new BufferedReader(new FileReader(file));
	} catch (Exception e) {
		e.printStackTrace();
	}
	c = read();
    }
//=============================================//
//=============================================//
    
//=============================================//
    private char read() {
		try {
			return (char) (reader.read());
		} catch (IOException e) {
			e.printStackTrace();
			return EOF;
		}
	}
//=============================================//
//=============================================//
	private int charClass;
//=============================================//
    private boolean isCharLetter(char c) {
			if(Character.isLetter(c)) {
				charClass = LETTER;
				return true;
			} else return false;
    }
//=============================================//
    private boolean isCharDigit(char c) {
		if(Character.isDigit(c)) {
			charClass = DIGIT;
			return true;
		} else return false;
}
//=============================================//
//=============================================//
    private boolean isCharUnknown(char c) {
		if(Character.isDigit(c)) {
			return false;
		} else if(Character.isLetter(c)) {
			return false;
		} else
			charClass = UNKNOWN;
			return true;
    }
//=============================================//
//=============================================//

//=============================================//
//lexical analyzer lexical arithmetic expressions                	       
//=============================================//
public Token nextToken() {
	int state = 1;
	int numBuffer = 0;
	String alphaBuffer = "";
	int decBuffer=0;
            boolean skipped = false;
	while (true) {
	if (c == EOF && !skipped) {
                    skipped = true;
                  
            }else if (skipped) {
                        
                        try {
                        
                                reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
                            
			return null;
		}
	switch(state) {
	case 1:
		switch(c) {
		case ' ':
		case '\n':
		case '\b':
		case '\f':
		case '\r':
		case '\t':	
			c = read();
			continue;
			
		case '(':
			c = read();
			return new Token(LEFT_PAREN, "(");
        
        case ')':
        	c = read();
			return new Token(RIGHT_PAREN, ")");
       	 
        case '+':
       	 	c = read(); 
       	 	return new Token(ADD_OP, "+");
            	 
        case '-':
       	 	c = read();
       	 	return new Token(SUB_OP, "-");
        
        case '*':
       	 	c = read();
       	 	return new Token(MULT_OP, "*");
       	 
        case '/':
       	 	c = read();
       	 	return new Token(DIV_OP, "/");
       
        case '=':
       	 	c = read();
       	 	return new Token(ASSIGN_OP, "=");
       	 
        default:
        	state = 2;
        	continue;
    }
	case 2:
		if(isCharDigit(c)) {
			numBuffer = 0;
			numBuffer += (c - '0');
			
			state = 3;
			c = read();
		}
	case 3:
		if (isCharDigit(c)) {
			numBuffer *= 10;
			numBuffer += (c - '0');

			c = read();
                                
		}else if(c=='.'){
			
			c = read();	
                               
			state=4;
                                
                                } else {
			return new Token(DIGIT, "" + numBuffer);
		}
                        
		continue;
			
	case 4:
		if (isCharDigit(c)) {
			decBuffer = 0;
			decBuffer += (c - '0');
			state=7;					
			c = read();	
                                
		}else  {
			return new Token(ERROR, "Invalid input: "+numBuffer+"." );
		}
		continue;

	case 7:
		if (isCharDigit(c)) {
			decBuffer *= 10;
			decBuffer += (c - '0');

			c = read();
		} else {
			return new Token(DIGIT, "" + numBuffer+"."+decBuffer);
		}
		continue;	

	case 5:
		if(isCharLetter(c)|| c=='_'){
		alphaBuffer = "";					
		alphaBuffer+=c;
		state=6;
		c = read();
		}else {
                            alphaBuffer = "";					
		    alphaBuffer+=c;
                                c=read();
			return new Token(ERROR, "Invalid input:"+alphaBuffer);
		}
		continue;	
	
	case 6:	
		if ((isCharLetter(c) || isCharLetter(c) || c=='_')) {
			
			alphaBuffer += c;
			c = read();                                                  
		}
		continue;
	}
}

}
//=============================================//
//=============================================//
}

//=============================================//
public class Main {
//=============================================//
//function mains                	           //
//=============================================//
	public static void main(String[] args) { 
		String inFile = "in.txt";
		String outFile = "out.txt";
	
		Lexer lexer = new Lexer(inFile);
	
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));

			Token t;

			while ((t = lexer.nextToken()) != null) {
				writer.write(t.toString());
				writer.newLine();
			}

			writer.close(); 
		
			System.out.println("Done tokenizing file: " + inFile);
			System.out.println("Output written in file: " + outFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
//=============================================//
//=============================================//
}
//=============================================//