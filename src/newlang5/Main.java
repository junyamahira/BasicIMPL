package newlang5;

import java.io.FileInputStream;

public class Main {

    public static void main(String[] args) throws Exception {
        FileInputStream fin = null;
        LexicalAnalyzer lex;
        LexicalUnit first;
        Environment env;
        Node program;

        System.out.println("==basic Interpreter==");
        try {
            fin = new FileInputStream("test2.bas");
        } catch (Exception e) {
            System.out.println("file not found");
            System.exit(-1);
        }
        lex = new LexicalAnalyzerImpl(fin);
        env = new Environment(lex);
        first = lex.get();
        lex.unget(first);

        program = ProgramNode.getHandler(env);

        if (program != null && program.Parse()) {
            //System.out.println(program);
        } else {
            System.out.println("syntax error");
        }
        System.out.println("execute");
        program.getValue();
        System.out.println("end");
    }

}
