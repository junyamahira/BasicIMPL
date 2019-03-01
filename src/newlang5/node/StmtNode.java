package newlang5.node;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import newlang5.Environment;
import newlang5.LexicalType;
import newlang5.Value;

public class StmtNode extends Node {

	private final static Set<LexicalType> FIRST =
			new HashSet<LexicalType>(Arrays.asList(LexicalType.NAME,
					LexicalType.FOR, LexicalType.END));
    private StmtNode(Environment env) {
        super(env);
        type = NodeType.STMT;
    }

    static boolean isMatch(LexicalType type) {
        return FIRST.contains(type);
    }

    public static Node getHandler(Environment env) throws Exception {
		//System.out.println("stmt");

		switch (env.getInput().peep().getType()) {
        case NAME:
            if (env.getInput().peep(2).getType() == LexicalType.EQ) {
                return SubstNode.getHandler(env);
            }
            if (ExprListNode.isMatch(env.getInput().peep(2).getType())) {
                return FunctionCallNode.getHandler(env);
            }
            throw new Exception("Stmt:解析中に構文エラー");
        case FOR:
            return ForStmtNode.getHandler(env);
        case END:
            return EndNode.getHandler(env);
        default:
            throw new Exception("Stmt:文の開始として不適切な型:" + env.getInput().peep().getType());
		}
    }

	@Override
	public boolean parse() throws Exception {
		throw new Exception("stmt:ParseはNG");
	}

    public Value getValue() throws Exception { //何かがおかしい
        return null;
    }

    public String toString() {
        return "This is a Stmt Node."; //呼ばれたら何かおかしい
    }
}
