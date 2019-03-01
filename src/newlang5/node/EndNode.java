package newlang5.node;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import newlang5.Environment;
import newlang5.LexicalType;
import newlang5.Value;


public class EndNode extends Node {
	static final Set<LexicalType> FIRST =
			new HashSet<LexicalType>(Arrays.asList(LexicalType.END));

    private EndNode(Environment env) {
        super(env);
        type = NodeType.END;
    }

    public static boolean isMatch(LexicalType type) {
        return FIRST.contains(type);
    }

    public static Node getHandler(Environment env) {
        return new EndNode(env);
    }

    @Override
    public boolean parse() throws Exception {
        if (env.getInput().peep().getType() == LexicalType.END) {
            env.getInput().get();
            return true;
        } else {
            throw new Exception("END:適切なタイミングで呼ばれていません");
        }
    }

    @Override
    public Value getValue() throws Exception {
        System.out.println("\nProgram END");
        return null;
    }

    @Override
    public String toString() {
        return "END";
    }
}
