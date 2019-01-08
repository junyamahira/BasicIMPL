package newlang4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProgramNode extends Node {

	static Set<LexicalType> first =
			new HashSet<LexicalType>(Arrays.asList(LexicalType.IF,
					LexicalType.WHILE, LexicalType.DO, LexicalType.NAME,
					LexicalType.FOR, LexicalType.END));

    List<Node> child = new ArrayList<Node>();

	public ProgramNode(Environment env) {
		super(env);
		type = NodeType.PROGRAM;
	}

	public static Node getHandler(Environment env) {
		return StmtListNode.getHandler(env);
	}

	public static boolean isMatch(LexicalType type) {
		return true;
	}

	@Override
	public String toString() {
		return "program node";
	}
}
