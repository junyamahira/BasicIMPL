package newlang4;

import java.util.HashSet;
import java.util.Set;

import newlang3.LexicalType;
import newlang3.LexicalUnit;

public class BlockNode {
	static Set<LexicalUnit> first =
			new HashSet(Araays.atList(LexicalType.DO, LexicalType.IF));

	static boolean isMatch (LexicalType type) {
		return first.contains(type);
	}

	static Node getHandler (LexicalType type){
		return null;
	}
}
