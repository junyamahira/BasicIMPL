package newlang4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StmtListNode extends Node{
	List<Node> child = new ArrayList<Node>();

	static Set<LexicalType> first =
			new HashSet<LexicalType>(Arrays.asList(LexicalType.IF,
					LexicalType.DO, LexicalType.WHILE, LexicalType.NL,
					LexicalType.NAME, LexicalType.FOR, LexicalType.END));

	public StmtListNode(Environment env) {
		super(env);
		type = NodeType.STMT_LIST;
	}

	public static boolean isMatch(LexicalType type){
		return first.contains(type);
	}

	public static Node getHandler(Environment env) {
		// TODO 自動生成されたメソッド・スタブ
		return new StmtListNode(env);
	}
}
