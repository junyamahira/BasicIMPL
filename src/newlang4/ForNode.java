package newlang4;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ForNode extends Node {
	static final Set<LexicalType> FIRST =
			new HashSet<LexicalType>(Arrays.asList(LexicalType.FOR));
	private Node substNode;
	private LexicalUnit max;
	private Node operation;
	private LexicalUnit name;

	private ForNode(Environment env){
		super(env);
		type = NodeType.FOR_STMT;
	}

	public static boolean isMatch(LexicalType type) {
		return FIRST.contains(type);
	}

	public static Node getHandler(Environment env) {
		return new ForNode(env);
	}

	@Override
	public boolean Parse() throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return super.Parse();
	}

	@Override
	public Value getValue() throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return super.getValue();
	}

	@Override
	public String toString() {
		// TODO 自動生成されたメソッド・スタブ
		return super.toString();
	}
}
