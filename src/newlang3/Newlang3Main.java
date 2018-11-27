package newlang3;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class Newlang3Main {
	public static void main(String[] args) {
		LexicalUnit lu;
		String fname = "test2.bas";
		LexicalAnalyzer la;
		InputStream is = null;

		if (args.length > 0) {
			fname = args[0];
		}

		try {
			is = new FileInputStream(fname);
		} catch (FileNotFoundException e1) {
			System.out.println(fname + ": not found");
			System.exit(-1);
		}

		la = new LexicalAnalyzerImpl(is);
		while (true) {
			try {
				lu = la.get();
				System.out.println(lu.toString());
				if (lu.getType() == LexicalType.EOF)break;
			} catch (Exception e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
		}
	}
}
