package newlang5.node;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import newlang5.LexicalType;
import newlang5.Value;

public class ConstNode extends Node {
	 private Value value;
	    static final Set<LexicalType> FIRST =
	    		new HashSet<LexicalType>(Arrays.asList(LexicalType.INTVAL, LexicalType.DOUBLEVAL,
	    				LexicalType.LITERAL, LexicalType.SUB));

    private ConstNode(Value v) {
        this.value = v;
        switch (v.getType()) {
            case INTEGER:
                type = NodeType.INT_CONSTANT;
                break;
            case DOUBLE:
                type = NodeType.DOUBLE_CONSTANT;
                break;
            case STRING:
                type = NodeType.STRING_CONSTANT;
                break;
            default:
                break;
        }
    }

    public static boolean isMatch(LexicalType type) {
        return FIRST.contains(type);
    }

    public static Node getHandler(Value v) {
        return new ConstNode(v);
    }

    @Override
    public boolean parse() throws Exception {
        return false;
    }

    @Override
    public Value getValue() throws Exception {
        return value;
    }

    public String toString() {
        String valueStr = "const: ";
        switch (type) {
            case INT_CONSTANT:
                valueStr += value.getIValue();
                break;
            case DOUBLE_CONSTANT:
                valueStr += value.getDValue();
                break;
            case STRING_CONSTANT:
                valueStr += value.getSValue();
                break;
            default :
                valueStr += "???";
                break;
        }
    	return valueStr;
    }
}
