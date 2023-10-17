package testbench.testutil.uses;

public class ExampleClass{
	
	public String foo="valor";
	public int i=5;
	
	public void example() {
		int other;
		
		System.out.println("hello "+foo);
	}
	
	public void example2() {
		int other;
		
		System.out.println("hello "+this.foo);
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
	
	public void yotromas() {
		String foo="bar";
		System.out.println("hello "+this.foo);
	}
	
	public void otromasaun() {
		String foo="bar";
		System.out.println("hello "+this.foo);
		
	}

	public void masprueba() {
		for(this.i=0;this.i<20;this.i++) {
			this.i++;
		}

	}
}