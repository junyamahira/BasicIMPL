package newlang4;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


public class BlockNode extends Node {
	static Set<LexicalType> first =
			new HashSet<LexicalType>(Arrays.asList(LexicalType.DO,
					LexicalType.WHILE, LexicalType.IF));

	static boolean isMatch (LexicalType type) {
		return first.contains(type);
	}

	static Node getHandler (Environment env) throws Exception{
		LexicalType inType = env.getInput().peep(1).getType();

		if (IfBlockNode.isMatch(inType)) return IfBlockNode.getHandler(env);
		else if (LoopNode.isMatch(inType)) return LoopNode.getHandler(env);
		else throw new Exception("blockNodeで不適切な型検出:" + env.getInput().peep(1).getType());
	}

	public BlockNode(Environment env) {
		super(env);
		type = NodeType.BLOCK;
	}

	@Override
	public String toString() {
		return "block";
	}
}
