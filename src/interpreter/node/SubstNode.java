package interpreter.node;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import interpreter.Environment;
import interpreter.LexicalType;
import interpreter.Value;

public class SubstNode extends Node {
	private final static Set<LexicalType> FIRST =
			new HashSet<LexicalType>(Arrays.asList(LexicalType.NAME));

	String leftstr;
	Node expr;

    private SubstNode(Environment env) {
        super(env);
        type = NodeType.ASSIGN_STMT;
    }

    static boolean isMatch(LexicalType type) {
        return FIRST.contains(type);
    }

    public static SubstNode getHandler(Environment env) {
        return new SubstNode(env);
    }

    @Override
    public boolean parse() throws Exception {
		if (env.getInput().expect(LexicalType.NAME)) {
            leftstr = env.getInput().get().getValue().getSValue();
        } else throw new Exception("Subst:代入文が不適切です");

        if (env.getInput().expect(LexicalType.EQ)) {
            env.getInput().get();
        }else throw new Exception("Subst:＝が見当たりません");

        if (ExprNode.isMatch(env.getInput().peep().getType())) {
            expr = ExprNode.getHandler(env);
            expr.parse();
        } else throw new Exception("Subst:代入文の後半が式として不適切です");

        return true;
    }

    @Override
    public Value getValue() throws Exception {
        env.getVariable(leftstr).setValue(expr.getValue());
        return null;
    }

    @Override
    public String toString() {
		// TODO 自動生成されたメソッド・スタブ
		return "SUBST:[" + expr + " -> " + leftstr + "]";
	}
}
