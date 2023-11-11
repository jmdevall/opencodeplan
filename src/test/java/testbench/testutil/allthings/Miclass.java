package testbench.testutil.allthings;

public class Miclass {
	
	private String foo;
	
	public Miclass(String foo) {
		this.foo=foo;
	}
	
	public String helloWorld(String who) {
		return "hello " + who + " foo="+foo;
	}

}
