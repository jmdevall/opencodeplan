package jmdevall.normal;

public class Main {
    
    public static double sqrt(double number, double precision) {
        double guess = number / 2.0;
        while (Math.abs(guess * guess - number) > precision) {
            guess = (guess + number / guess) / 2.0;
        }
        return guess;
    }

    public static double exp(double number, double precision) {
        double sum = 0.0;
        int i = 0;
        while (Math.abs(Math.pow(number, i) / factorial(i)) > precision) {
            sum += Math.pow(number, i) / factorial(i);
            i++;
        }
        return sum;
    }

    public static int factorial(int n) {
        int factorial = 1;
        for (int i = 1; i <= n; i++) {
            factorial *= i;
        }
        return factorial;
    }

    public static double pow(double base, double exponent, double precision) {
        double integerPart = (int) exponent;
        double fractionalPart = exponent - integerPart;
        double result = 1.0;

        for (int i = 0; i < Math.abs(integerPart); i++) {
            result *= base;
        }

        if (fractionalPart != 0) {
            double root = sqrt(base, precision);
            for (int i = 0; i < Math.abs((int) (fractionalPart * 1000000)); i++) {
                result *= root;
            }
        }

        if (exponent < 0) {
            result = 1 / result;
        }

        return result;
    }

}