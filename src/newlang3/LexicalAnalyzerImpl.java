package newlang3;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class LexicalAnalyzerImpl implements LexicalAnalyzer {


	public LexicalAnalyzerImpl(String fname) {
		File f = new File(fname);
		try {
			Scanner sc = new Scanner(f);
		} catch (FileNotFoundException e) {
			System.out.println(fname + "not found");
			System.exit(-1);
		}
	}

	@Override
	public LexicalUnit get() throws Exception {
		return null;
	}

	@Override
	public boolean expect(LexicalType type) throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

	@Override
	public void unget(LexicalUnit token) throws Exception {
		// TODO 自動生成されたメソッド・スタブ

	}

	private LexicalUnit getString() throws Exception{
		return null;
	}

}
