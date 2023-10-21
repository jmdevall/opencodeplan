package jmdevall.opencodeplan.domain.dependencygraph;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class RangeTest {
	
	@Test
	public void inrange() {
	Range r=Range.newRange(0, 10, 3, 5);
	
	Range r2=Range.newRange(1, 0, 2, 3);
	
	assertTrue(r.contains(r2));
	
	assertFalse(r2.contains(r));
	
	assertTrue(r.contains(r));
	}
}
