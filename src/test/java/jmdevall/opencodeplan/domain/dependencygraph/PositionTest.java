package jmdevall.opencodeplan.domain.dependencygraph;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class PositionTest {

	@Test
	public void comparePositionsIgual() {
		Position a = Position.newPosition(10, 0);
		Position b = Position.newPosition(10, 0);

		assertEquals(0, Position.comparator().compare(a, b));
	}

	@Test
	public void comparePositionsMenor() {
		Position a = Position.newPosition(9, 0);
		Position b = Position.newPosition(10, 0);

		assertEquals(-1, Position.comparator().compare(a, b));

		a = Position.newPosition(9, 20);
		b = Position.newPosition(10, 0);

		assertEquals(-1, Position.comparator().compare(a, b));

		a = Position.newPosition(10, 20);
		b = Position.newPosition(10, 30);

		assertEquals(-1, Position.comparator().compare(a, b));

	}

	@Test
	public void comparePositionsMayor() {
		Position a = Position.newPosition(10, 0);
		Position b = Position.newPosition(9, 0);

		assertEquals(1, Position.comparator().compare(a, b));

		a = Position.newPosition(11, 10);
		b = Position.newPosition(10, 0);

		assertEquals(1, Position.comparator().compare(a, b));

		a = Position.newPosition(11, 10);
		b = Position.newPosition(11, 0);

		assertEquals(1, Position.comparator().compare(a, b));

	}

	@Test
	public void nose() {
		assertEquals(1, Integer.compare(10, 9));
	}
	
	@Test
	public void positionAbsolute() {
		String text="12345\n789";
		
		assertEquals(0,Position.newPosition(1, 1).absolute(text));
		assertEquals(2,Position.newPosition(1, 3).absolute(text));
		assertEquals(6,Position.newPosition(2, 1).absolute(text));
		
	}
	
	@Test
	public void positionAbsolute2() {
		String text="12345";
		
		assertEquals(0,Position.newPosition(1, 1).absolute(text));
		assertEquals(2,Position.newPosition(1, 3).absolute(text));
		
	}
	
	@Test
	public void positionAbsolute3() {
		String text="12345\n789\n012";
		
		assertEquals(0,Position.newPosition(1, 1).absolute(text));
		assertEquals(2,Position.newPosition(1, 3).absolute(text));
		
	}
	
	String clase=
	  "package testbench.testutil.overrides;\n"
	+ "\n"
	+ "public class A {\n"
	+ "\n"
	+ "public void foo() {\n"
	+ "    System.out.println(\"foo\");\n"
	+ "    \n}"
	+ "\n}";
	@Test
	public void positionAbsoluteClase() {
		
		assertEquals(0,Position.newPosition(1, 1).absolute(clase));
		
	}
	
}
