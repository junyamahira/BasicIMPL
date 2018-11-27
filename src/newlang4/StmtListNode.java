package newlang4;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.omg.CORBA.Environment;

import newlang3.LexicalType;

public class StmtListNode extends Node{
	Lexical<Node> child = new ArrayList<Node>();
	static Set<LexicalType> first = new HashSet<LexicalType>(Arrays.asList(LexicalType.IF, LexicalType.DO, LexicalType.FOR, LexicalType.END));

	public StmtListNode(Environment e) {
		super(e);
	}

	static boolean isMatch(LexicalType t){
		return first.contains(e)
	}
}
