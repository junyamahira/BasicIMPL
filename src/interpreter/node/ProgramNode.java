package interpreter.node;


import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import interpreter.Environment;
import interpreter.LexicalType;
import interpreter.Value;

public class ProgramNode extends Node {

	static final Set<LexicalType> FIRST =
			new HashSet<LexicalType>(Arrays.asList(LexicalType.IF,
					LexicalType.WHILE, LexicalType.DO, LexicalType.NAME,
					LexicalType.FOR, LexicalType.END));

    public static Node getHandler(LexicalType t, Environment env) throws Exception {
        return StmtListNode.getHandler(env);
    }

    public String toString() {
        return "Program Node.";
    }

    @Override
    public Value getValue() throws Exception {
        throw new Exception("Program:getValueはNG");
    }
}
