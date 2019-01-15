package newlang5;


/**
 *
 * @author tago
 */
public class Function {

    /** Creates a new instance of Function */
    public Function() {
    }

    public Value invoke(ExprListNode arg) {

    	for (int i = 0; i < arg.size(); i++) {
			Value val = arg.getElemnt(i);
			System.out.println(val.getSValue());
		}
    	return null;

    }

}
