package newlang5.node;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import newlang5.Environment;
import newlang5.LexicalType;
import newlang5.LexicalUnit;
import newlang5.Value;
import newlang5.ValueImpl;

public class ForStmtNode extends Node {
	static final Set<LexicalType> FIRST =
			new HashSet<LexicalType>(Arrays.asList(LexicalType.FOR));
	private Node subst;
	private LexicalUnit target;
	private Node operation;
	private LexicalUnit name;;

    private ForStmtNode(Environment env) {
        super(env);
        type = NodeType.FOR_STMT;
    }

    public static boolean isMatch(LexicalType type) {
        return FIRST.contains(type);
    }

    public static Node getHandler(Environment env) {
        return new ForStmtNode(env);
    }

    @Override
    public boolean parse() throws Exception {
		//forチェック
		if(!env.getInput().expect(LexicalType.FOR)) throw new Exception("For:forが見当たりません");
		env.getInput().get();

		//subst(条件式)チェック
		if (SubstNode.isMatch(env.getInput().peep(1).getType())) {
			this.subst = SubstNode.getHandler(env);
			subst.parse();
		} else {
			throw new Exception("For:for文の開始エラー");
		}

		//toチェック
		if (!env.getInput().expect(LexicalType.TO)) throw new Exception("For:toが見当たりません");
		env.getInput().get();

		//INTVALチェック
		if (env.getInput().expect(LexicalType.INTVAL)) target = env.getInput().get();
		else throw new Exception("For:終了値がありません");

		//NLチェック
		if (!env.getInput().expect(LexicalType.NL)) throw new Exception("For:改行が見当たりません(前)");
        skipNL();

        if (StmtListNode.isMatch(env.getInput().peep().getType())) {
            operation = StmtListNode.getHandler(env);
            operation.parse();
        } else throw new Exception("FOR文中に適切な文がありません");
        skipNL();

		//nextチェック
		if (!env.getInput().expect(LexicalType.NEXT)) throw new Exception("For:NEXTが見当たりません");
        env.getInput().get();
        skipNL();

		//nameチェック
		if (env.getInput().expect(LexicalType.NAME)) name = env.getInput().get();
		else throw new Exception("For:変数が見当たりません");

        return true;
    }

    @Override
    public Value getValue() throws Exception {
        SubstNode subst = (SubstNode) this.subst;
        subst.getValue();
        VariableNode controlVar = env.getVariable(name.getValue().getSValue());

        int step = target.getValue().getDValue() > subst.expr.getValue().getDValue() ? 1 : -1;
        while (true) {
            operation.getValue();
            controlVar.setValue(new ExprNode(controlVar, ConstNode.getHandler(new ValueImpl(step)), LexicalType.ADD).getValue());
            if (controlVar.getValue().getDValue() > target.getValue().getDValue()) break;
        }

        return null;
    }

    public String toString() {
		String str ="";
		str += "FOR文: ("+ subst  +"TO" + target + "){\n";
		str += operation + "\n";
		str += "}\n";
		str += ":" + name;
		return str;
    }
}
