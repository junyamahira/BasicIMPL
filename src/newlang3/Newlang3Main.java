package newlang3;

public class Newlang3Main {
	public static void main(String[] args) {
		String fname = "test1.bas";
		if (args.length > 0) {
			fname = args[0];
		}
//		File f1 = new File(fname);
//
//		Scanner scan;
//		try {
//			scan = new Scanner(f1);
//			String str;
//			while (scan.hasNextLine()) {
//				str = scan.nextLine();
//				for (int i = 0; i < str.length(); i++) {
//					System.out.print(str.charAt(i));
//				}
//				System.out.println();
//			}
//			scan.close();
//
//		} catch (FileNotFoundException e) {
//			System.out.println(fname + "not found");
//			System.exit(-1);
//		}

		LexicalAnalyzer la = new LexicalAnalyzerImpl(fname);
        while(true){
        	try{
        		LexicalUnit lu = la.get();
        		System.out.println(lu);
        		if(lu.getType()==LexicalType.EOF)break;
        	}catch(Exception e){
        		e.printStackTrace();
        		break;
        	}
        }
	}
}
