package newlang4;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class EndNode extends Node {
	static final Set<LexicalType> FIRST =
			new HashSet<LexicalType>(Arrays.asList(LexicalType.END));

	public EndNode(Environment env) {
		super(env);
		type = NodeType.END;
	}

	public static boolean isMatch(LexicalType type) {
		return FIRST.contains(type);
	}

	public static Node getHandler(Environment env) {
		return new EndNode(env);
	}

	@Override
	public boolean Parse() throws Exception {
		if (env.getInput().expect(LexicalType.END)) {
			env.getInput().get();
			return true;
		}else {
			throw new Exception("ENDが見当たりません");
		}
	}

	@Override
	public String toString() {
		return "END";
	}


}
