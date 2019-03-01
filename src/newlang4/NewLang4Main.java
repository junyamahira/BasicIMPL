package newlang4;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class NewLang4Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		FileInputStream fin = null;
		LexicalAnalyzer lex;
		LexicalUnit first;
		Environment env;
		Node program;

		System.out.println("basic parser");

		String fname = "test1.bas";
		if (args.length > 0) {
			fname = args[0];
		}

		try {
			fin = new FileInputStream(fname);
		} catch (FileNotFoundException e1) {
			System.out.println(fname + ": not found");
			System.exit(-1);
		}

		lex = new LexicalAnalyzerImpl(fin);
		env = new Environment(lex);
		first = lex.get();
		lex.unget(first);

		program = ProgramNode.getHandler(env);
		if (program != null && program.Parse()) {
			System.out.println(program);
			//System.out.println("value = " + program.getValue());
		} else
			System.out.println("syntax error");
	}

}
