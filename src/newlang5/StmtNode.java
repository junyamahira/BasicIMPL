package newlang5;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class StmtNode extends Node{

	private final static Set<LexicalType> FIRST =
			new HashSet<LexicalType>(Arrays.asList(LexicalType.NAME,
					LexicalType.FOR, LexicalType.END));

	private StmtNode(Environment env){
		super(env);
		super.type = NodeType.STMT;
	}

	public static boolean isMatch(LexicalType type) {
		return FIRST.contains(type);
	}

	static Node getHandler(Environment env) throws Exception{
		System.out.println("stmt");

		switch (env.getInput().peep(1).getType()) {
        case NAME:
            if (env.getInput().peep(2).getType() == LexicalType.EQ) {
                return SubstNode.getHandler(env);
            }
            if (ExprListNode.isMatch(env.getInput().peep(2).getType())) {
                return CallSubFuncNode.getHandler(env);
            }
            throw new Exception("Stmt:解析中に構文エラー");
        case FOR:
            return ForNode.getHandler(env);
        case END:
            return EndNode.getHandler(env);
        default:
            throw new Exception("Stmt:文の開始として不適切な型:" + env.getInput().peep(1).getType());
		}
	}

	@Override
	public boolean Parse() throws Exception {
		throw new Exception("stmtNodeのParseは呼ばれません");
	}

	@Override
	public String toString() {
		// TODO 自動生成されたメソッド・スタブ
		return "Stmt";
	}

	@Override
	public Value getValue() throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

}
