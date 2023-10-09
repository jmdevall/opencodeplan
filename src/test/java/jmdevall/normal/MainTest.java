package jmdevall.normal;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MainTest {
    
    @Test
    public void testSqrt() {
        double number = 22.0;
        double precision = 0.000001;
        double result = Main.sqrt(number, precision);
        assertEquals(Math.sqrt(number), result, precision);
    }

    @Test
    public void testExp() {
        double number = 7.0;
        double precision = 0.000001;
        double result = Main.exp(number, precision);
        assertEquals(Math.exp(number), result, precision);
    }

    @Test
    public void testPow() {
        double base = 2.0;
        double exponent = 3.5;
        double precision = 0.000001;
        double result = Main.pow(base, exponent, precision);
        assertEquals(Math.pow(base, exponent), result, precision);
    }
}
