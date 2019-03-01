package newlang4;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class CondNode extends Node {
	private final static Set<LexicalType> FIRST =
			new HashSet<LexicalType>(Arrays.asList(LexicalType.NAME, LexicalType.SUB,
					LexicalType.LP, LexicalType.INTVAL, LexicalType.LITERAL));
	private final static Set<LexicalType> OPERATORS =
			new HashSet<LexicalType>(Arrays.asList(LexicalType.EQ, LexicalType.LT,
					LexicalType.LE, LexicalType.GT, LexicalType.GE, LexicalType.NE));
	private Node left;
	private Node right;
	private LexicalType operator;

	public CondNode(Environment env) {
		super(env);
		type = NodeType.COND;
	}

	public static boolean isMatch(LexicalType type) {
		return FIRST.contains(type);
	}

	public static Node getHandler(Environment env) {
		return new CondNode(env);
	}

	@Override
	public boolean Parse() throws Exception {
		if (ExprNode.isMatch(env.getInput().peep(1).getType())) {
			left = ExprNode.getHandler(env);
			left.Parse();
		}else throw new Exception("Cond:左辺処理中にエラー");

		if (OPERATORS.contains(env.getInput().peep(1).getType())) {
			operator = env.getInput().get().getType();
		}else  throw new Exception("Cond:不適切な記号");


        if (ExprNode.isMatch(env.getInput().peep(1).getType())) {
            right = ExprNode.getHandler(env);
            right.Parse();
        } else throw new Exception("Cond:右辺処理中にエラー");

        return true;

	}

	@Override
	public String toString() {
		return "(" + left + " " + operator + " " + right + ")";
	}
}
