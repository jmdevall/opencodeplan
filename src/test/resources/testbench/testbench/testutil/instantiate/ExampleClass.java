package testbench.testutil.instantiate;

public class ExampleClass{
	
	public ExampleClass() {
		//constructor
		System.out.println("hello");
	}
	
	public void foo() {
		new ExampleClass();
	}
	
}