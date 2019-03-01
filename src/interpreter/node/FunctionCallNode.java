package newlang5.node;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import funcs.Function;
import interpreter.Environment;
import interpreter.LexicalType;
import interpreter.Value;

public class FunctionCallNode extends Node {
	public static final Set<LexicalType> FIRST =
			new HashSet<LexicalType>(Arrays.asList(LexicalType.NAME));
    String funcName;
    Node arguments;

    private FunctionCallNode(Environment env) {
        super(env);
        type = NodeType.FUNCTION_CALL;
    }

    public static boolean isMatch(LexicalType type) {
        return FIRST.contains(type);
    }

    public static Node getHandler(Environment env) {
        return new FunctionCallNode(env);
    }

    @Override
    public boolean parse() throws Exception {
        boolean isBracket = false;
        funcName = env.getInput().get().getValue().getSValue();

        if (env.getInput().expect(LexicalType.LP)) {
            isBracket = true;
            env.getInput().get();
        }

        if (ExprListNode.isMatch(env.getInput().peep().getType())) {
            arguments = ExprListNode.getHandler(env);
            arguments.parse();
        }

        if (isBracket && !env.getInput().expect(LexicalType.RP))
            throw new Exception("Func:対応する()が閉じられていません");

        if (isBracket && env.getInput().expect(LexicalType.RP))
            env.getInput().get();

        return true;
    }

    @Override
    public Value getValue() throws Exception {
        Function func = env.getFunction(funcName);
        if (func == null) {
            throw new Exception("Func:()が閉じられていません");
        }
        ExprListNode arg = (ExprListNode) arguments;
        return func.invoke(arg);
    }

    public String toString() {
        return "func: " + funcName + "(" + arguments + ")";
    }
}
