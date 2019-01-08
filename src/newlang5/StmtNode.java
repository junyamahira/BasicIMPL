package newlang5;

import java.util.HashSet;
import java.util.Set;

public class StmtNode extends Node{
	Node body;

	private StmtNode(Environment env){
		super(env);
		super.type = NodeType.STMT;
	}


	private static Set<LexicalType> firstSet = new HashSet<LexicalType>();
	static {
		firstSet.add(LexicalType.NAME);
		firstSet.add(LexicalType.FOR);
		firstSet.add(LexicalType.END);
	}

	public static Node isMatch(Environment env, LexicalUnit first) {
		if(!firstSet.contains(first.type)){
			return null;
		}

		return new StmtNode(env);
	}

	@Override
	public boolean Parse() throws Exception {
		LexicalUnit lu = env.getInput().get();
		env.getInput().unget(lu);

		body = SubstNode.isMatch(env, lu);
		if(body != null){
			return body.Parse();
		}

		body = CallSubNode.isMatch(env, lu);
		if(body != null){
			return body.Parse();
		}

		if (lu.getType() == LexicalType.END) {
			super.type = NodeType.END;
			return true;
		}

		return false;

	}

}
