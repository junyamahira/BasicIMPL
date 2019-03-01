package newlang4;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ProgramNode extends Node {

	static Set<LexicalType> first =
			new HashSet<LexicalType>(Arrays.asList(LexicalType.IF,
					LexicalType.WHILE, LexicalType.DO, LexicalType.NAME,
					LexicalType.FOR, LexicalType.END));

	//???とりま放置
    //List<Node> child = new ArrayList<Node>();

	public ProgramNode(Environment env) {
		super(env);
		type = NodeType.PROGRAM;
	}

	public static Node getHandler(Environment env) {
		return StmtListNode.getHandler(env);
	}

	public static boolean isMatch(LexicalType type) {
		return first.contains(type);
	}

	@Override
	public String toString() {
		return "program node";
	}

	@Override
	public Value getValue() throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		throw new Exception("Program:getValueはNG");
	}
}
