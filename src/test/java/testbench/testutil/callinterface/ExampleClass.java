package testbench.testutil.callinterface;

public class ExampleClass {
	ExampleInterface unfield;
	
	public ExampleClass(ExampleInterface unfield){
		this.unfield=unfield;
	}
	
	public void dosomething() {
		this.unfield.foo("bar");
	}

}
