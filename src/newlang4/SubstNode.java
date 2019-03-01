package newlang4;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class SubstNode extends Node {

	private final static Set<LexicalType> FIRST =
			new HashSet<LexicalType>(Arrays.asList(LexicalType.NAME));

	String leftstr;
	Node expr;

	public SubstNode(Environment env) {
		super(env);
		super.type = NodeType.ASSIGN_STMT;
	}

	static boolean isMatch(LexicalType type) {
		return FIRST.contains(type);
	}

	public static SubstNode getHandler(Environment env) {
		return new SubstNode(env);
	}

	@Override
	public boolean Parse() throws Exception {
		if (env.getInput().expect(LexicalType.NAME)) {
            leftstr = env.getInput().get().getValue().getSValue();
        } else throw new Exception("Subst:代入文が不適切です");

        if (env.getInput().expect(LexicalType.EQ)) {
            env.getInput().get();
        }else throw new Exception("Subst:＝見当たりません");

        if (ExprNode.isMatch(env.getInput().peep(1).getType())) {
            expr = ExprNode.getHandler(env);
            expr.Parse();
        } else throw new Exception("Subst:代入文の後半が式として不適切です");

        return true;
    }


	@Override
	public String toString() {
		// TODO 自動生成されたメソッド・スタブ
		return "SUBST:[" + expr + " -> " + leftstr + "]";
	}

	@Override
	public Value getValue() throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return super.getValue();
	}

}
