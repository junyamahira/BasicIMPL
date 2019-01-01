package newlang4;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PushbackReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LexicalAnalyzerImpl implements LexicalAnalyzer {

	PushbackReader pbr;
	private static final Map<String, LexicalUnit> RESERVED_WORDS = new HashMap<>();

	private List<LexicalUnit> lexicalUnits = new ArrayList<>();

	static {
		RESERVED_WORDS.put("IF", new LexicalUnit(LexicalType.IF));
		RESERVED_WORDS.put("THEN", new LexicalUnit(LexicalType.THEN));
		RESERVED_WORDS.put("FOR", new LexicalUnit(LexicalType.FOR));
		RESERVED_WORDS.put("FORALL", new LexicalUnit(LexicalType.FORALL));
		RESERVED_WORDS.put("NEXT", new LexicalUnit(LexicalType.NEXT));
		RESERVED_WORDS.put("FUNC", new LexicalUnit(LexicalType.FUNC));
		RESERVED_WORDS.put("DIM", new LexicalUnit(LexicalType.DIM));
		RESERVED_WORDS.put("AS", new LexicalUnit(LexicalType.AS));
		RESERVED_WORDS.put("END", new LexicalUnit(LexicalType.END));
		RESERVED_WORDS.put("WHILE", new LexicalUnit(LexicalType.WHILE));
		RESERVED_WORDS.put("DO", new LexicalUnit(LexicalType.DO));
		RESERVED_WORDS.put("UNTIL", new LexicalUnit(LexicalType.UNTIL));
		RESERVED_WORDS.put("LOOP", new LexicalUnit(LexicalType.LOOP));
		RESERVED_WORDS.put("TO", new LexicalUnit(LexicalType.TO));
		RESERVED_WORDS.put("=", new LexicalUnit(LexicalType.EQ));
		RESERVED_WORDS.put("<", new LexicalUnit(LexicalType.LT));
		RESERVED_WORDS.put(">", new LexicalUnit(LexicalType.GT));
		RESERVED_WORDS.put("<=", new LexicalUnit(LexicalType.LE));
		RESERVED_WORDS.put("=<", new LexicalUnit(LexicalType.LE));
		RESERVED_WORDS.put(">=", new LexicalUnit(LexicalType.GE));
		RESERVED_WORDS.put("=>", new LexicalUnit(LexicalType.GE));
		RESERVED_WORDS.put("<>", new LexicalUnit(LexicalType.NE));
		RESERVED_WORDS.put("\n", new LexicalUnit(LexicalType.NL));
		RESERVED_WORDS.put("\r", new LexicalUnit(LexicalType.NL));
		RESERVED_WORDS.put(".", new LexicalUnit(LexicalType.DOT));
		RESERVED_WORDS.put("+", new LexicalUnit(LexicalType.ADD));
		RESERVED_WORDS.put("-", new LexicalUnit(LexicalType.SUB));
		RESERVED_WORDS.put("*", new LexicalUnit(LexicalType.MUL));
		RESERVED_WORDS.put("/", new LexicalUnit(LexicalType.DIV));
		RESERVED_WORDS.put("(", new LexicalUnit(LexicalType.LP));
		RESERVED_WORDS.put(")", new LexicalUnit(LexicalType.RP));
		RESERVED_WORDS.put(",", new LexicalUnit(LexicalType.COMMA));
		RESERVED_WORDS.put("WEND", new LexicalUnit(LexicalType.WEND));
		RESERVED_WORDS.put("ENDIF", new LexicalUnit(LexicalType.ENDIF));
		RESERVED_WORDS.put("ELSE", new LexicalUnit(LexicalType.ELSE));
		RESERVED_WORDS.put("ELSEIF", new LexicalUnit(LexicalType.ELSEIF));
	}

	public LexicalAnalyzerImpl(InputStream is) {
		pbr = new PushbackReader(new InputStreamReader(is));
	}

	@Override
	public LexicalUnit get() throws Exception {
		int ci;

		do {
			ci = pbr.read();
		} while (ci == ' ' || ci == '\t');

		if (ci == -1)
			return new LexicalUnit(LexicalType.EOF);
		else
			pbr.unread(ci);

		if ((ci >= 'a' && ci <= 'z') || (ci >= 'A' && ci <= 'Z')) {
			return getString();
		} else if (ci >= '0' && ci <= '9') {
			return getNum();
		} else if (ci == '\"') {
			return getLiteral();
		} else if (RESERVED_WORDS.containsKey(String.valueOf((char) ci))) {
			return getSymbol();
		} else {
			throw new Exception("文字読み込みエラー");
		}
	}

	private LexicalUnit getString() throws Exception {
		String cursor = "";
		int ci;
		char ch;

		while (true) {
			ci = pbr.read();
			ch = (char) ci;
			if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z')) {
				cursor += ch;
				continue;
			}

			if (ch >= '0' && ch <= '9') {
				cursor += ch;
				continue;
			}
			if (!(ci == ' ' || ci == '\t'))
				pbr.unread(ci);
			break;
		}

		if (RESERVED_WORDS.containsKey(cursor))
			return RESERVED_WORDS.get(cursor);
		return new LexicalUnit(LexicalType.NAME, new ValueImpl(cursor, ValueType.STRING));
	}

	private LexicalUnit getSymbol() throws IOException {
		String cursor = "";
		int ci;
		char ch;

		ci = pbr.read();
		ch = (char) ci;
		cursor += ch;

		if (RESERVED_WORDS.containsKey(cursor)) {
			if (ch == '<' || ch == '>' || ch == '=') {
				ci = pbr.read();
				ch = (char) ci;
				if (RESERVED_WORDS.containsKey(cursor + ch))
					return RESERVED_WORDS.get(cursor + ch);
				else {
					pbr.unread(ci);
					return RESERVED_WORDS.get(cursor);
				}
			} else if (ch == '\r') {
				ci = pbr.read();
				ch = (char) ci;
				if (RESERVED_WORDS.containsKey(cursor))
					return RESERVED_WORDS.get(cursor);
			} else
				return RESERVED_WORDS.get(cursor);
		}
		return null;
	}

	private LexicalUnit getLiteral() throws Exception {
		String cursor = "";
		int ci = pbr.read();

		while (true) {
			ci = pbr.read();
			if (ci == '\"')
				break;
			if (ci == -1)
				return new LexicalUnit(LexicalType.EOF);
			else
				cursor += (char) ci;
		}
		return new LexicalUnit(LexicalType.LITERAL, new ValueImpl(cursor, ValueType.STRING));
	}

	private LexicalUnit getNum() throws IOException {
		String cursor = "";
		boolean isFloat = false;
		int ci;
		char ch;

		while (true) {
			ci = pbr.read();
			ch = (char) ci;
			if (ch >= '0' && ch <= '9') {
				cursor += ch;
				continue;
			}
			if ((ch == '.') && !isFloat) {
				cursor += ch;
				isFloat = true;
				continue;
			}
			pbr.unread(ci);
			break;
		}

		if (isFloat)
			return new LexicalUnit(LexicalType.DOUBLEVAL, new ValueImpl(cursor, ValueType.DOUBLE));
		return new LexicalUnit(LexicalType.INTVAL, new ValueImpl(cursor, ValueType.INTEGER));
	}

	@Override
	public boolean expect(LexicalType type) throws Exception {
		return type == peep(1).getType();
	}

	@Override
	public void unget(LexicalUnit token) throws Exception {
		lexicalUnits.add(token);
	}

	public LexicalUnit peep(int n) throws Exception{
		List<LexicalUnit> tmp = new ArrayList<>();

		for (int i = 0; i < n -1; i++) {
			tmp.add(get());
		}
		LexicalUnit lu = get();
		unget(lu);

        while(!tmp.isEmpty()) {
            unget(tmp.get(tmp.size() - 1));
            tmp.remove(tmp.size() - 1);
        }

        return lu;
	}
}
