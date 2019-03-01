package newlang4;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class LoopNode extends Node {

	private Node condition;
	private Node op;
	private boolean isDo = false;
	private boolean untilFlg = false;

	static final Set<LexicalType> FIRST = new HashSet<LexicalType>(
			Arrays.asList(LexicalType.DO, LexicalType.WHILE));

	public LoopNode(Environment env) {
		super(env);
		super.type = NodeType.LOOP_BLOCK;
	}

	public static boolean isMatch(LexicalType type) {
		return FIRST.contains(type);
	}

	public static Node getHandler(Environment env) {
		return new LoopNode(env);
	}

	@Override
	public boolean Parse() throws Exception {
		if (env.getInput().expect(LexicalType.WHILE)) {
			env.getInput().get();

			if (CondNode.isMatch(env.getInput().peep(1).getType())) {
				condition = CondNode.getHandler(env);
				condition.Parse();
			}else throw new Exception("Loop:条件式が見当たりません");

			//NLチェック
			if (!env.getInput().expect(LexicalType.NL)) throw new Exception("Loop:条件式の後に改行が見当たりません");

			//stmt_listチェック
			if (StmtListNode.isMatch(env.getInput().peep(1).getType())) {
				op = StmtListNode.getHandler(env);
				op.Parse();
			}else throw new Exception("Loop:処理部分が見当たりません");

			//NLチェック
			if (!env.getInput().expect(LexicalType.NL)) throw new Exception("Loop:処理部分の後に改行が見当たりません");

			//WENDチェック
			if (!env.getInput().expect(LexicalType.WEND)) throw new Exception("Loop:WENDが見当たりません");

		}else if (env.getInput().expect(LexicalType.DO)) {
			env.getInput().get();

			isDo = true;

			if (env.getInput().expect(LexicalType.UNTIL)) untilFlg = true;
			env.getInput().get();

			//条件式チェック
			if (CondNode.isMatch(env.getInput().peep(1).getType())){
				condition = CondNode.getHandler(env);
				condition.Parse();
			}else throw new Exception("Loop:条件式が見当たりません");

			//NLチェック
			if (!env.getInput().expect(LexicalType.NL)) throw new Exception("Loop:処理部分の前にNLが見当たりません");

			//stmt_listチェック
			if (StmtListNode.isMatch(env.getInput().peep(1).getType())) {
				op = StmtListNode.getHandler(env);
				op.Parse();
			}else throw new Exception("Loop:処理部分が見当たりません");

			//NLチェック
			if (!env.getInput().expect(LexicalType.NL)) throw new Exception("Loop:処理部分の後に改行が見当たりません");

			//LOOPチェック
			if (!env.getInput().expect(LexicalType.LOOP)) throw new Exception("Loop:LOOPが見当たりません");

			//coditionチェック
			if (condition == null) {
				if (!env.getInput().expect(LexicalType.UNTIL) && !env.getInput().expect(LexicalType.WHILE)){
					throw new Exception("Loop:WHILEかUNTILで始まる条件式が見当たりません");
				}
			}

		}else throw new Exception("Loop:最初のWHILEかDOが見当たりません");

		//NLチェック
		if (env.getInput().expect(LexicalType.NL)) throw new Exception("Loop:最後の改行が見当たりません");

		return true;
	}

	@Override
	public String toString() {
        String str = "LOOP: ";
        str += "condition = (";
        if (untilFlg) str += "!";
        str += condition + ")\n";
        str += op;
        return str;
	}

	public Value getValue() throws Exception {
		return null;
	}
}
