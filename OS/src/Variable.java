public class Variable {
	String name;
	Object value;

	public Variable(String name, Object value) {
		this.name = name;
		this.value = value;
	}

	@Override
	public String toString() {
		return "Variable [name=" + name + ", value=" + value + "]";
	}

}