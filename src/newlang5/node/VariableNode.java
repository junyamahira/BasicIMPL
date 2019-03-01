package newlang5.node;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import newlang5.LexicalType;
import newlang5.LexicalUnit;
import newlang5.Value;

public class VariableNode extends Node {
	public static final Set<LexicalType> FIRST =
			new HashSet<LexicalType>(Arrays.asList(LexicalType.NAME));
	String name;
	Value val;

    public VariableNode(String name) {
        type = NodeType.VARIABLE;
        this.name = name;
    }

    public VariableNode(LexicalUnit lu) {
        type = NodeType.VARIABLE;
        name = lu.getValue().getSValue();
    }

    public static boolean isMatch(LexicalType type) {
        return FIRST.contains(type);
    }

    @Override
    public boolean parse() {
        return false;
    }

    @Override
    public String toString() {
        return "Valable:" + name;
    }

    public void setValue(Value val) {
        this.val = val;
    }

    public Value getValue() throws Exception {
        if (val == null) {
            throw new Exception("Variable:初期化されていない変数");
        }
        return val;
    }
}
