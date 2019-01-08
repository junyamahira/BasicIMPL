package newlang5;

import java.io.FileInputStream;

import newlang4.LexicalAnalyzerImpl;
import newlang4.ProgramNode;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	       FileInputStream fin = null;
	        LexicalAnalyzer lex;
	        LexicalUnit		first;
	        Environment		env;
	        Node			prog;

	        System.out.println("basic parser");
	        try {
	            fin = new FileInputStream("test.txt");
	        }
	        catch(Exception e) {
	            System.out.println("file not found");
	            System.exit(-1);
	        }
	        lex = new LexicalAnalyzerImpl(fin);
	        env = new Environment(lex);

	        prog = (Node) new ProgramNode(env);
	        if (prog != null && prog.Parse()) {
	        	prog.getValue();
	        }
	        else System.out.println("syntax error");
	}

}
