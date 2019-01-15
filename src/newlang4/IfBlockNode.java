package newlang4;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class IfBlockNode extends Node {
	static final Set<LexicalType> FIRST = new HashSet<LexicalType>(Arrays.asList(LexicalType.IF));
	private Node condition;
	private Node trueNode;
	private Node elseNode;
	boolean isElseIF = false;

	public IfBlockNode(Environment env) {
		super(env);
		super.type = NodeType.IF_BLOCK;
	}

	static boolean isMatch(LexicalType type) {
		return FIRST.contains(type);
	}

	static Node getHandler(Environment env) {
		return new IfBlockNode(env);
	}

	@Override
	public Value getValue() throws Exception {
        if (condition.getValue().getBValue()) trueNode.getValue();
        else if (elseNode != null) elseNode.getValue();
        return null;
	}

	@Override
	public String toString() {
        String str = "";
        str += String.format("IF(%s)  THEN\n%s", condition, trueNode);
        if (elseNode != null) str += String.format("\nELSE\n%s", elseNode);
        if (isElseIF) str += "\nENDIF\n";

        return str;
	}

	@Override
	public boolean Parse() throws Exception {
		LexicalType inType = env.getInput().peep(1).getType();

		env.getInput().get();
		if (inType == LexicalType.IF || inType == LexicalType.ELSEIF) {
			if (inType == LexicalType.ELSEIF) isElseIF = true;

			if (CondNode.isMatch(env.getInput().peep(1).getType())) {
				condition = CondNode.getHandler(env);
				condition.Parse();
			} else throw new Exception("条件式が見当たりません");

			if (env.getInput().get().getType() != LexicalType.THEN)
				throw new Exception("THENが見つかりません");

		}else {
			 throw new Exception("IF,ELSEIFが見つかりません");
		}

		//true時
		if (StmtNode.isMatch(env.getInput().peep(1).getType())) {
			trueNode = StmtNode.getHandler(env);
			trueNode.Parse();

			if (env.getInput().expect(LexicalType.ELSE)) {
				env.getInput().get();

				if (StmtNode.isMatch(env.getInput().peep(1).getType())) {
					elseNode = StmtNode.getHandler(env);
					elseNode.Parse();
				}else throw new Exception("ELSE処理中でエラー");
			}

			if (!env.getInput().expect(LexicalType.NL))
				throw new Exception("IF；NLが見当たりません");
			return true;

		//NL
		}else if (env.getInput().expect(LexicalType.NL)) {
			env.getInput().get();

			if (StmtListNode.isMatch(env.getInput().peep(1).getType())) {
				trueNode = StmtListNode.getHandler(env);
				trueNode.Parse();
			}else throw new Exception("ELSE：true処理中にエラー");

			//ELSEIF&ELSE
			if (env.getInput().expect(LexicalType.ELSEIF)) {
				elseNode = IfBlockNode.getHandler(env);
				elseNode.Parse();
			}else if (env.getInput().expect(LexicalType.ELSE)) {
				env.getInput().get();

				if (env.getInput().expect(LexicalType.NL)) {
					if (StmtListNode.isMatch(env.getInput().peep(1).getType())) {
						elseNode = StmtListNode.getHandler(env);
						elseNode.Parse();
					}else throw new Exception("ELSE：false処理中にエラー");
				}else throw new Exception("ELSE:NLが見当たりません");
			}
		}else throw new Exception("ELSEIFかELSEが見当たりません");

		if(!isElseIF){
			if (!env.getInput().expect(LexicalType.NL)) throw new Exception("ELSEの最後のNLが見当たりません");
		}

		if (!env.getInput().expect(LexicalType.ENDIF))throw new Exception("ENDIFが見当たりません");
		return true;
	}
}
