package interpreter.node;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import interpreter.Environment;
import interpreter.LexicalType;
import interpreter.Value;

//わからん 復習
public class LoopBlockNode extends Node {
	static final Set<LexicalType> FIRST = new HashSet<LexicalType>(
			Arrays.asList(LexicalType.DO, LexicalType.WHILE));

    private Node condition;
    private Node op;
    private boolean isDo;
    private boolean untilFlg;

    private LoopBlockNode(Environment env) {
        super(env);
        type = NodeType.LOOP_BLOCK;
    }

    public static boolean isMatch(LexicalType type) {
        return FIRST.contains(type);
    }

    public static Node getHandler(Environment env) {
        return new LoopBlockNode(env);
    }

    @Override
    public boolean parse() throws Exception {
        if (env.getInput().expect(LexicalType.WHILE)) {
            isDo = false;
            checkCond();
            checkOperation();

            skipNL();
            if (!env.getInput().expect(LexicalType.WEND)) {
                throw new Exception("Loop:WHILE文末にWENDがありません");
            }
            env.getInput().get();
            if (!env.getInput().expect(LexicalType.NL)) {
                throw new Exception("Loop:WHILE文末に改行がありません");
            }
            env.getInput().get();

        } else if (env.getInput().peep().getType() == LexicalType.DO) {
            env.getInput().get();
            switch (env.getInput().peep().getType()) {
                case NL:
                    isDo = true;
                    checkOperation();
                    if (!env.getInput().expect(LexicalType.LOOP)) {
                        throw new Exception("Loop:DO_LOOP構文の中に\"LOOP\"がありません");
                    }
                    env.getInput().get();

                    checkCond();
                    if (!env.getInput().expect(LexicalType.NL)) {
                        throw new Exception("Loop:DO_LOOP文末に改行がありません");
                    }
                    skipNL();
                    break;

                case WHILE:
                case UNTIL:
                    isDo = false;
                    checkCond();
                    checkOperation();

                    if (!env.getInput().expect(LexicalType.LOOP)) {
                        throw new Exception(String.format("Loop:DO_%s_LOOP文末に\"LOOP\"がありません", untilFlg ? "WHILE" : "UNTIL"));
                    }
                    env.getInput().get();

                    if (!env.getInput().expect(LexicalType.NL)) {
                        throw new Exception(String.format("Loop:DO_%s_LOOP文末に改行がありません", untilFlg ? "WHILE" : "UNTIL"));
                    }
                    skipNL();

                    break;
                default:
                    throw new Exception("Loop:DO直後に適切な型がありません");
            }
        } else {
            throw new Exception("Loop:適切ではない型がLOOP処理の先頭に現れました:" + env.getInput().peep().getType());
        }

        return true;
    }

    public Value getValue() throws Exception {
        if (isDo) {
            do {
                op.getValue();
            } while (condition.getValue().getBValue() == untilFlg);
        } else {
            while (condition.getValue().getBValue() == untilFlg) {
                op.getValue();
            }
        }
        return null;
    }

    public String toString() {
        if (isDo) {
            return String.format("%s{\n%s\n}\n%s%s\n",
                    isDo ? "DO" : "LOOP",
                    op,
                    untilFlg ? "" : "!",
                    condition);
        } else {
            return String.format("%s%s%s{\n%s\n}\n",
                    isDo ? "DO" : "LOOP",
                    untilFlg ? "" : " !",
                    condition,
                    op);
        }
    }

    private boolean checkCond() throws Exception {
        if (env.getInput().expect(LexicalType.WHILE)) {
            untilFlg = true;
        } else if (env.getInput().expect(LexicalType.UNTIL)) {
            untilFlg = false;
        } else {
            throw new Exception("Loop:適切な条件判定がありません");
        }

        env.getInput().get();
        if (CondNode.isMatch(env.getInput().peep().getType())) {
            condition = CondNode.getHandler(env);
            condition.parse();
        } else {
            throw new Exception(String.format("Loop:%s文中に制御文がありません", untilFlg ? "WHILE" : "UNTIL"));
        }
        return true;
    }

    private boolean checkOperation() throws Exception {
        if (!env.getInput().expect(LexicalType.NL)) {
            throw new Exception("Loop:処理部の前に改行がありません");
        }
        skipNL();
        if (StmtListNode.isMatch(env.getInput().peep().getType())) {
            op = StmtListNode.getHandler(env);
            op.parse();
        } else {
            throw new Exception("Loop:繰り返し文中に処理部がありません");
        }
        return true;
    }
}
