package newlang5;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class CallSubFuncNode extends Node {
	public static final Set<LexicalType> FIRST =
			new HashSet<LexicalType>(Arrays.asList(LexicalType.NAME));
    String funcName;
    Node arguments;

    private CallSubFuncNode(Environment env) {
        super(env);
        type = NodeType.FUNCTION_CALL;
    }

    public static boolean isMatch(LexicalType type) {
        return FIRST.contains(type);
    }

    public static Node getHandler(Environment env) {
        return new CallSubFuncNode(env);
    }

    public boolean parse() throws Exception {
    	boolean isBracket = false;
        funcName = env.getInput().get().getValue().getSValue();

        if (env.getInput().expect(LexicalType.LP)) {
            isBracket = true;
            env.getInput().get();
        }
        if (ExprListNode.isMatch(env.getInput().peep(1).getType())) {
            arguments = ExprListNode.getHandler(env);
            arguments.Parse();
        }

    	if (isBracket && env.getInput().expect(LexicalType.RP))
    		throw new Exception("CallSub:()が閉じられていません");
    	else if (isBracket && env.getInput().expect(LexicalType.RP))
    		env.getInput().get();

        return true;
    }

    @Override
    public Value getValue() throws Exception {
        Function func = env.getFunction(funcName);
        if (func == null) {
            throw new Exception("CallSub:存在しない関数です");
        }
        ExprListNode arg = (ExprListNode) arguments;
        return func.invoke(arg);
    }

    public String toString() {
        return "func: " + funcName + "(" + arguments + ")";
    }
}