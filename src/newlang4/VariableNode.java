package newlang4;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class VariableNode extends Node {
	public static final Set<LexicalType> FIRST =
			new HashSet<LexicalType>(Arrays.asList(LexicalType.NAME));
	String name;
	Value val;

	public VariableNode(String name) {
		type = NodeType.VARIABLE;
		this.name = name;
	}

	public VariableNode(String name, Value val) {
		type = NodeType.VARIABLE;
		this.name = name;
		this.val = val;
	}

	public static boolean isMatch(LexicalType type) {
        return FIRST.contains(type);
    }

	@Override
	public boolean Parse() throws Exception {return false;}

	@Override
	public String toString() {
	    return "Variable:" + name;
	}

	public void setVal(Value val) {
		this.val = val;
	}

	@Override
	public Value getValue() throws Exception {
		return val;
	}


}
