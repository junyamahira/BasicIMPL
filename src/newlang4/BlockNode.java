package newlang4;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


public class BlockNode extends Node {
	static Set<LexicalUnit> first =
			new HashSet(Arrays.asList(LexicalType.DO, 
					LexicalType.WHILE, LexicalType.IF));

	
	static boolean isMatch (LexicalType type) {
		return first.contains(type);
	}
	
	static Node getHandler (LexicalType type){
		return null;
	}
	
	public BlockNode(Environment env) {
		super(env);
		type = NodeType.BLOCK;
	}
}
