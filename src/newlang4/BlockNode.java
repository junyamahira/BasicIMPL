package newlang4;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


public class BlockNode extends Node {
	private static final Set<LexicalType> FIRST =
			new HashSet<LexicalType>(Arrays.asList(LexicalType.DO,
					LexicalType.WHILE, LexicalType.IF));

	static boolean isMatch (LexicalType type) {
		return FIRST.contains(type);
	}

	static Node getHandler (Environment env) throws Exception{
		System.out.println("block");

		LexicalType inType = env.getInput().peep(1).getType();

		if (IfBlockNode.isMatch(inType)) return IfBlockNode.getHandler(env);
		else if (LoopNode.isMatch(inType)) return LoopNode.getHandler(env);
		else throw new Exception("block: 不適切な型検出:" + env.getInput().peep(1).getType());
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
