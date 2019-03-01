package newlang5.node;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import newlang5.Environment;
import newlang5.LexicalType;
import newlang5.Value;
import newlang5.ValueImpl;
import newlang5.ValueType;

public class CondNode extends Node {
	private final static Set<LexicalType> FIRST =
			new HashSet<LexicalType>(Arrays.asList(LexicalType.NAME, LexicalType.SUB,
					LexicalType.LP, LexicalType.INTVAL, LexicalType.LITERAL));
	private Node left;
	private Node right;
	private LexicalType operator;

	//比較演算子
    private final static Map<LexicalType, String> OPERATORS = new HashMap<>();
    static {
        OPERATORS.put(LexicalType.EQ, "=");
        OPERATORS.put(LexicalType.GT, ">");
        OPERATORS.put(LexicalType.LT, "<");
        OPERATORS.put(LexicalType.GE, ">=");
        OPERATORS.put(LexicalType.LE, "<=");
        OPERATORS.put(LexicalType.NE, "!=");
    }

    private CondNode(Environment env) {
        super(env);
        type = NodeType.COND;
    }

    public static boolean isMatch(LexicalType type) {
        return FIRST.contains(type);
    }

    public static Node getHandler(Environment env) {
        return new CondNode(env);
    }

    @Override
    public boolean parse() throws Exception {
		if (ExprNode.isMatch(env.getInput().peep().getType())) {
			left = ExprNode.getHandler(env);
			left.parse();
		}else throw new Exception("Cond:左辺処理中にエラー");

        if (OPERATORS.containsKey(env.getInput().peep().getType())) {
            operator = env.getInput().get().getType();
        } else throw new Exception("Cond:不適切な記号 = " + env.getInput().peep().getType());

        if (ExprNode.isMatch(env.getInput().peep().getType())) {
            right = ExprNode.getHandler(env);
            right.parse();
        } else throw new Exception("Cond:右辺処理中にエラー");

        return true;
    }

    @Override
    public Value getValue() throws Exception {
        Value val1 = left.getValue();
        Value val2 = right.getValue();

        if (val1 == null || val2 == null) {
            throw new Exception("Cond:比較文にnullがあります");
        }

        if ((val1.getType() == ValueType.INTEGER || val1.getType() == ValueType.DOUBLE)
                && (val2.getType() == ValueType.INTEGER || val2.getType() == ValueType.DOUBLE)) {
            if (operator == LexicalType.LT) {
                return new ValueImpl(val1.getDValue() < val2.getDValue());
            } else if (operator == LexicalType.LE) {
                return new ValueImpl(val1.getDValue() <= val2.getDValue());
            } else if (operator == LexicalType.GT) {
                return new ValueImpl(val1.getDValue() > val2.getDValue());
            } else if (operator == LexicalType.GE) {
                return new ValueImpl(val1.getDValue() >= val2.getDValue());
            } else if (operator == LexicalType.EQ) {
                return new ValueImpl(val1.getDValue() == val2.getDValue());
            } else if (operator == LexicalType.NE) {
                return new ValueImpl(val1.getDValue() != val2.getDValue());
            } else {
                throw new Exception("Cond:不正な比較演算子");
            }
        }

        if (val1.getType() == ValueType.STRING && val2.getType() == ValueType.STRING) {
            if (operator == LexicalType.EQ) {
                return new ValueImpl(val1.getSValue().equals(val2.getSValue()));
            } else if (operator == LexicalType.NE) {
                return new ValueImpl(!val1.getSValue().equals(val2.getSValue()));
            } else {
                throw new Exception("Cond:不正な文字列比較演算子");
            }
        }

        if (val1.getType() == ValueType.BOOL && val2.getType() == ValueType.BOOL) {
            if (operator == LexicalType.EQ) {
                return new ValueImpl(val1.getBValue() == val2.getBValue());
            } else if (operator == LexicalType.NE) {
                return new ValueImpl(val1.getBValue() != val2.getBValue());
            } else {
                throw new Exception("Cond:不正な真偽値比較演算子");
            }
        }
        return new ValueImpl(false);
    }

    public String toString() {
        return "(" + left + " " + OPERATORS.get(operator) + " " + right + ")";
    }
}
