package newlang5.node;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import interpreter.Environment;
import interpreter.LexicalType;
import interpreter.Value;

public class StmtListNode extends Node {
    List<Node> child = new ArrayList<Node>();
	static final Set<LexicalType> FIRST =
			new HashSet<LexicalType>(Arrays.asList(LexicalType.IF,
					LexicalType.DO, LexicalType.WHILE, LexicalType.NL,
					LexicalType.NAME, LexicalType.FOR, LexicalType.END));

    private StmtListNode(Environment env) {
        super(env);
        type = NodeType.STMT_LIST;
    }

    static boolean isMatch(LexicalType t) {
        return FIRST.contains(t);
    }

    public static Node getHandler(Environment env) {
        return new StmtListNode(env);
    }

    @Override
    public boolean parse() throws Exception {
        while (true) {
            skipNL();
            Node candidate;
            if (StmtNode.isMatch(env.getInput().peep().getType())) {
                candidate = StmtNode.getHandler(env);
            } else if (BlockNode.isMatch(env.getInput().peep().getType())) {
                candidate = BlockNode.getHandler(env);
            } else {
                break; //解析終了
            }
            candidate.parse();
            child.add(candidate);
        }
        return true;
    }

    @Override
    public Value getValue() throws Exception {
        for (Node node : child) {
            node.getValue();
            if (node.getType() == NodeType.END) {
                return null;
            }
        }
        return null;
    }

    @Override
    public String toString() {
		// TODO 自動生成されたメソッド・スタブ
        String str = "";
        for (int i = 0; i < child.size(); i++) {
            str += child.get(i).toString();
            if (i != child.size() - 1) {
                str += "\n";
            }
        }
        return str;
    }
}
