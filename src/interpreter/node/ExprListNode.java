package newlang5.node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import interpreter.Environment;
import interpreter.LexicalType;
import interpreter.Value;

public class ExprListNode extends Node {
	static final Set<LexicalType> FIRST =
			new HashSet<LexicalType>(Arrays.asList(LexicalType.NAME,
					LexicalType.SUB, LexicalType.LP, LexicalType.INTVAL,
					LexicalType.DOUBLEVAL, LexicalType.LITERAL));
	List<Node> child = new ArrayList<Node>();

    private ExprListNode(Environment env) {
        super(env);
        type = NodeType.EXPR_LIST;
    }

    public static boolean isMatch(LexicalType type) {
        return FIRST.contains(type);
    }

    public static Node getHandler(Environment env) {
        return new ExprListNode(env);
    }

    @Override
    public boolean parse() throws Exception {
		while (true) {
			if (ExprNode.isMatch(env.getInput().peep().getType())) {
				Node expr = ExprNode.getHandler(env);
				expr.parse();
				child.add(expr);
			} else {
				throw new Exception("ExprList:不適切な式");
			}

			if (env.getInput().expect(LexicalType.COMMA)) {
				env.getInput().get();
				continue;
			} else {
				break;
			}
		}
		return true;
    }

    @Override
    public Value getValue() throws Exception {
        throw new Exception("ExprList:getValueはNG");
        //return null;
    }

    @Override
    public String toString() {
		String str = "";
		for (Node s : child) {
			str += s.toString() + " ";
		}
		return str;
    }

    public List<Node> getChild() {
        return child;
    }
}
