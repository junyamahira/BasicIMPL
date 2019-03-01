package newlang5;

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

	@Override
	public boolean Parse() throws Exception {
		throw new Exception("Block:Parse実行NG");
	}

	@Override
	public Value getValue() throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		throw new Exception("Block:getValue実行NG");
	}
}
