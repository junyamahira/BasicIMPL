package interpreter;

import java.io.FileInputStream;

import interpreter.node.Node;
import interpreter.node.ProgramNode;

public class Main {

    public static void main(String[] args) throws Exception {
        FileInputStream fin = null;
        LexicalAnalyzer lex;
        LexicalUnit first;
        Environment env;
        Node program;

        System.out.println("-basic Interpreter strat-");
        try {
            fin = new FileInputStream("test1.bas");
        } catch (Exception e) {
            System.out.println("file not found");
            System.exit(-1);
        }
        lex = new LexicalAnalyzerImpl(fin);
        env = new Environment(lex);
        first = lex.get();
        lex.unget(first);

        program = ProgramNode.getHandler(env.getInput().peep().getType(), env);

        if (program != null && program.parse()) {
            //System.out.println(program);
        } else {
            System.out.println("syntax error");
        }
        System.out.println("execute");
        program.getValue();
        System.out.println("-basic Interpreter end-");
    }

}
