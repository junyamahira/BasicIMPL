package newlang4;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class IfBlockNode extends Node {
	static Set<LexicalUnit> first =
			new HashSet(Arrays.asList(LexicalType.IF));
}
