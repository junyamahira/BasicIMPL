package newlang4;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ExprNode extends Node {
	static final Set<LexicalType> FIRST =
			new HashSet<LexicalType>(Arrays.asList(LexicalType.SUB,
					LexicalType.LP, LexicalType.NAME, LexicalType.INTVAL,
					LexicalType.DOUBLEVAL, LexicalType.LITERAL));

}
