package newlang4;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class CallSubFuncNode extends Node {
	public static final Set<LexicalType> FIRST =
			new HashSet<LexicalType>(Arrays.asList(LexicalType.NAME));
    String funcName;
    Node arguments;

    public CallSubFuncNode(Environment env) {
    	super(env);
    	super.type = NodeType.FUNCTION_CALL;
	}


    public static boolean isMatch(LexicalType type) {
        return FIRST.contains(type);
    }

    public static Node getHandler(Environment env) {
        return new CallSubFuncNode(env);
    }

    @Override
    public boolean Parse() throws Exception {
    	// TODO 自動生成されたメソッド・スタブ
    	return super.Parse();
    }

    @Override
    public String toString() {
    	return "func: " + funcName + "(" + arguments + ")\n";
    }

    @Override
    public Value getValue() throws Exception {
    	// TODO 自動生成されたメソッド・スタブ
    	return super.getValue();
    }
}
