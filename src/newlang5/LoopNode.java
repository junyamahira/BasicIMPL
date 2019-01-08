package newlang5;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class LoopNode extends Node {

	private Node condition;
	private Node op;
	private boolean isDo;
	private boolean flg;

	static final Set<LexicalType> FIRST = new HashSet<LexicalType>(
			Arrays.asList(LexicalType.DO, LexicalType.WHILE));

	public LoopNode(Environment env) {
		super(env);
		super.type = NodeType.LOOP_BLOCK;
	}

	public static boolean isMatch(LexicalType type) {
		return FIRST.contains(type);
	}

	public static Node getHandler(Environment env) {
		return new LoopNode(env);
	}

	@Override
	public boolean Parse() throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return super.Parse();
	}

	@Override
	public String toString() {
		// TODO 自動生成されたメソッド・スタブ
		return super.toString();
	}

	public Value getValue() throws Exception {
		return null;
	}

	private boolean checkCondition() {
		return false;
	}
}
