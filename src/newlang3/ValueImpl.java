package newlang3;

public class ValueImpl implements Value {

	ValueType type;
	int ivalue;
	double dvalue;
	String svalue;
	boolean bvalue;

	public ValueImpl(String src, ValueType targetType) {
		this.type = targetType;
		switch (targetType) {
		case INTEGER:

			break;
		case DOUBLE:
			break;
		case STRING:
			break;
		case BOOL:
			break;

		default:
			break;
		}
	}

	@Override
	public String getSValue() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public int getIValue() {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}

	@Override
	public double getDValue() {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}

	@Override
	public boolean getBValue() {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

	@Override
	public ValueType getType() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

}
