package newlang5;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ExprListNode extends Node {
	static final Set<LexicalType> FIRST =
			new HashSet<LexicalType>(Arrays.asList(LexicalType.NAME,
					LexicalType.SUB, LexicalType.LP, LexicalType.INTVAL,
					LexicalType.DOUBLEVAL, LexicalType.LITERAL));

	public ExprListNode(Environment env) {
		// TODO 自動生成されたコンストラクター・スタブ
	}
}
