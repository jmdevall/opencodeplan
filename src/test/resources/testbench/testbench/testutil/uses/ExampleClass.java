package testbench.testutil.uses;

public class ExampleClass{
	
	public String foo="valor";
	
	public void example() {
		int other;
		
		System.out.println("hello "+foo);
	}
	
	public void otro() {
		String foo="bar";
		
		System.out.println("hello "+foo);
	}
	
	public void otromas() {
		String foo="bar";
		this.foo="ooo";
		System.out.println("hello "+foo);
	}
	
	public void otromasaun() {
		String foo="bar";
		System.out.println("hello "+this.foo);
	}
	
}