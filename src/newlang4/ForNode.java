package newlang4;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ForNode extends Node {
	static final Set<LexicalType> FIRST =
			new HashSet<LexicalType>(Arrays.asList(LexicalType.FOR));
	private Node subst;
	private LexicalUnit max;
	private Node operation;
	private LexicalUnit name;

	private ForNode(Environment env){
		super(env);
		type = NodeType.FOR_STMT;
	}

	public static boolean isMatch(LexicalType type) {
		return FIRST.contains(type);
	}

	public static Node getHandler(Environment env) {
		return new ForNode(env);
	}

	@Override
	public boolean Parse() throws Exception {
		//forチェック
		if(!env.getInput().expect(LexicalType.FOR)) throw new Exception("For:forが見当たりません");
		env.getInput().get();

		//subst(条件式)チェック
		if (SubstNode.isMatch(env.getInput().peep(1).getType())) {
			this.subst = SubstNode.getHandler(env);
			subst.Parse();
		} else {
			throw new Exception("For:for文の開始エラー");
		}

		//toチェック
		if (!env.getInput().expect(LexicalType.TO)) throw new Exception("For:toが見当たりません");
		env.getInput().get();

		//INTVALチェック
		if (env.getInput().expect(LexicalType.INTVAL)) max = env.getInput().get();
		else throw new Exception("For:終了値がありません");


		//NLチェック
		if (!env.getInput().expect(LexicalType.NL)) throw new Exception("For:改行が見当たりません(前)");

		if (StmtListNode.isMatch(env.getInput().get().getType())){
			this.operation = StmtListNode.getHandler(env);
			operation.Parse();
		} else throw new Exception("For:文中に適切な文が見当たりません");

		//NLチェック
		if (!env.getInput().expect(LexicalType.NL)) throw new Exception("For:改行が見当たりません(後)");

		//nextチェック
		if (!env.getInput().expect(LexicalType.NEXT)) throw new Exception("For:NEXTが見当たりません");

		//nameチェック
		if (env.getInput().expect(LexicalType.NAME)) name = env.getInput().get();
		else throw new Exception("For:変数が見当たりません");

		return true;
	}

	@Override
	public Value getValue() throws Exception {
		//後で
		return null;
	}

	@Override
	public String toString() {
		String str ="";
		str += "FOR文: ("+ subst  +"TO" + max + "){\n";
		str += operation + "\n";
		str += "}\n";
		str += ":" + name;
		return str;

	}
}
