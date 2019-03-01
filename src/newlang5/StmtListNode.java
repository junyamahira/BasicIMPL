package newlang5;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StmtListNode extends Node{
	List<Node> child = new ArrayList<Node>();

	static Set<LexicalType> first =
			new HashSet<LexicalType>(Arrays.asList(LexicalType.IF,
					LexicalType.DO, LexicalType.WHILE, LexicalType.NL,
					LexicalType.NAME, LexicalType.FOR, LexicalType.END));

	public StmtListNode(Environment env) {
		super(env);
		type = NodeType.STMT_LIST;
	}

	public static boolean isMatch(LexicalType type){
		return first.contains(type);
	}

	public static Node getHandler(Environment env) {
		// TODO 自動生成されたメソッド・スタブ
		return new StmtListNode(env);
	}

	@Override
	public boolean Parse() throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		while (true) {
			skipNL();
            Node candidate;

            System.out.println("stmtlist");

            if (StmtNode.isMatch(env.getInput().peep(1).getType())) {
            	System.out.println("a");
                candidate = StmtNode.getHandler(env);
            } else if (BlockNode.isMatch(env.getInput().peep(1).getType())) {
            	System.out.println("b");
                candidate = BlockNode.getHandler(env);
            } else {
            	System.out.println("c");
                break; //解析終了
            }
            candidate.Parse();
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
