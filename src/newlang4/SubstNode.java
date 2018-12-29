package newlang4;

import java.util.HashSet;
import java.util.Set;

public class SubstNode extends Node {

	String leftstr;
	Node expr;

	static Set<LexicalType> first = new HashSet<LexicalType>();
	static{
		first.add(LexicalType.NAME);
	}

	public SubstNode(Environment env) {
		super(env);
		super.type = NodeType.ASSIGN_STMT;
	}

	static boolean isMatch(LexicalType type) {
		return first.contains(type);
	}

	public static SubstNode getHandler(Environment env) {
		return new SubstNode(env);
	}

	@Override
	public boolean Parse() throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return super.Parse();
	}

}
