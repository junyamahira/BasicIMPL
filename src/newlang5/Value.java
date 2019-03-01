package newlang5;

public interface Value {
	public String getSValue();
    public int getIValue();
    public double getDValue();
    public boolean getBValue();
    public ValueType getType();
    public String toString();
}
