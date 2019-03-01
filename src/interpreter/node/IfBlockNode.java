package interpreter.node;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import interpreter.Environment;
import interpreter.LexicalType;
import interpreter.Value;

public class IfBlockNode extends Node {
    static final Set<LexicalType> FIRST = new HashSet<LexicalType>(Arrays.asList(LexicalType.IF));
    private Node condtion;
    private Node trueNode;
    private Node elseNode;
    boolean isElseIF = false;


    private IfBlockNode(Environment env) {
        super(env);
        type = NodeType.IF_BLOCK;
    }

    static boolean isMatch(LexicalType type) {
        return FIRST.contains(type);
    }

    public static Node getHandler(Environment env) {
        return new IfBlockNode(env);
    }

    @Override
    public boolean parse() throws Exception {
		LexicalType inType = env.getInput().peep().getType();

		if (inType == LexicalType.IF || inType == LexicalType.ELSEIF) {
			if (inType == LexicalType.IF) isElseIF = true;
			env.getInput().get();
			if (CondNode.isMatch(env.getInput().peep().getType())) {
				condtion = CondNode.getHandler(env);
				condtion.parse();
			} else throw new Exception("If:条件式が見当たりません");

			if (env.getInput().get().getType() != LexicalType.THEN)
				throw new Exception("If:THENが見つかりません");

		}else {
			 throw new Exception("If:IF,ELSEIFが見つかりません");
		}

        if (StmtNode.isMatch(env.getInput().peep().getType())) {
            trueNode = StmtNode.getHandler(env);
            trueNode.parse();

            if (env.getInput().expect(LexicalType.ELSE)) {
                env.getInput().get();

                if (StmtNode.isMatch(env.getInput().peep().getType())) {
                    elseNode = StmtNode.getHandler(env);
                    elseNode.parse();
                } else {
                    throw new Exception("If:ELSE文の処理中にエラー");
                }
            }
            if (!env.getInput().expect(LexicalType.NL)) {
                throw new Exception("If:IF文末に改行が見当たりません");
            }
            env.getInput().get();
            return true;    //終了
        } else if (env.getInput().expect(LexicalType.NL)) {
            env.getInput().get();
            if (StmtListNode.isMatch(env.getInput().peep().getType())) {
                trueNode = StmtListNode.getHandler(env);
                trueNode.parse();
            } else {
                throw new Exception("If:TRUE時の処理の解析中にエラー");
            }
            while (env.getInput().expect(LexicalType.NL)) {
                env.getInput().get();   //？
            }

            if (env.getInput().expect(LexicalType.ELSEIF)) {
                elseNode = IfBlockNode.getHandler(env);
                elseNode.parse();
            } else if (env.getInput().expect(LexicalType.ELSE)) {
                env.getInput().get();
                if (!env.getInput().expect(LexicalType.NL)) {
                    throw new Exception("ELSE文直後に改行がありません");
                }
                skipNL();
                if (StmtListNode.isMatch(env.getInput().peep().getType())) {
                    elseNode = StmtListNode.getHandler(env);
                    elseNode.parse();
                } else {
                    throw new Exception("ELSE文のFALSE時の処理の解析中にエラーが起きました");
                }

                while (env.getInput().expect(LexicalType.NL)) {
                    env.getInput().get();
                }
            }
            if (isElseIF) {
                if (env.getInput().expect(LexicalType.ENDIF)) {
                    env.getInput().get();
                } else {
                    throw new Exception("If:ELSE、ENDIFがありません");
                }
                if (env.getInput().expect(LexicalType.NL)) {
                    env.getInput().get();   //OK
                } else {
                    throw new Exception("If:ELSE節の最後の改行が見当たりません");
                }
            }
        } else {
            throw new Exception("If:不適切な条件文");
        }
        return true;
    }

    @Override
    public Value getValue() throws Exception {
        if (condtion.getValue().getBValue()) {
            trueNode.getValue();
        } else {
            if (elseNode != null) {
                elseNode.getValue();
            }
        }

        return super.getValue();
    }

    @Override
    public String toString() {
        String str = "";
        str += String.format("IF(%s)  THEN\n%s", condtion, trueNode);
        if (elseNode != null) {
            str += String.format("\nELSE\n%s", elseNode);
        }
        if (isElseIF) {
            str += "\nENDIF\n";
        }
        return str;
    }
}
