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
    	boolean isBracket = false;
    	LexicalType inType = env.getInput().peep(1).getType();
    	funcName = env.getInput().get().getValue().getSValue();

    	if (env.getInput().expect(LexicalType.LP)){
    		isBracket = true;
    		env.getInput().get();
    	}

    	if (ExprListNode.isMatch(inType)) {
			arguments = ExprListNode.getHandler(env);
			arguments.Parse();
		}

    	if (isBracket && env.getInput().expect(LexicalType.RP))
    		throw new Exception("()が閉じられていません");
    	else if (isBracket && env.getInput().expect(LexicalType.RP))
    		env.getInput().get();

    	return true;
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
