package com.calculator;

import java.util.Scanner;

/**
 * Scientific Calculator - SPE Mini Project
 *
 * A menu-driven scientific calculator supporting:
 * 1. Add - a + b
 * 2. Subtract - a - b
 * 3. Multiply - a * b
 * 4. Divide - a / b
 * 5. Power - x^b
 * 6. Square Root - √x
 * 7. Logarithm - ln(x)
 * 8. Factorial - x!
 */
public class ScientificCalculator {

    // ───────────────────── Arithmetic Operations ─────────────────────

    /**
     * Returns the sum of two numbers.
     */
    public static double add(double a, double b) {
        return a + b;
    }

    /**
     * Returns the difference of two numbers (a - b).
     */
    public static double subtract(double a, double b) {
        return a - b;
    }

    /**
     * Returns the product of two numbers.
     */
    public static double multiply(double a, double b) {
        return a * b;
    }

    /**
     * Returns the quotient of two numbers (a / b).
     *
     * @throws IllegalArgumentException if b is zero
     */
    public static double divide(double a, double b) {
        if (b == 0) {
            throw new IllegalArgumentException("Cannot divide by zero.");
        }
        return a / b;
    }

    // ───────────────────── Scientific Operations ─────────────────────

    /**
     * Computes x raised to the power b.
     *
     * @param x the base
     * @param b the exponent
     * @return x^b
     */
    public static double power(double x, double b) {
        return Math.pow(x, b);
    }

    /**
     * Computes the square root of a non-negative number.
     *
     * @param x the input value (must be >= 0)
     * @return √x
     * @throws IllegalArgumentException if x < 0
     */
    public static double squareRoot(double x) {
        if (x < 0) {
            throw new IllegalArgumentException("Cannot compute square root of a negative number.");
        }
        return Math.sqrt(x);
    }

    /**
     * Computes the natural logarithm (base e) of a positive number.
     *
     * @param x the input value (must be > 0)
     * @return ln(x)
     * @throws IllegalArgumentException if x <= 0
     */
    public static double naturalLog(double x) {
        if (x <= 0) {
            throw new IllegalArgumentException("Natural logarithm is not defined for zero or negative numbers.");
        }
        return Math.log(x);
    }

    /**
     * Computes the factorial of a non-negative integer.
     * Uses long to support values up to 20!
     *
     * @param n the input value (must be >= 0)
     * @return n!
     * @throws IllegalArgumentException if n < 0 or n > 20
     */
    public static long factorial(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Factorial is not defined for negative numbers.");
        }
        if (n > 20) {
            throw new IllegalArgumentException("Input too large. Maximum supported value is 20.");
        }
        long result = 1;
        for (int i = 2; i <= n; i++) {
            result *= i;
        }
        return result;
    }

    // ──────────────────────── Menu-Driven Main ─────────────────────────

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("=============================================");
        System.out.println("       SCIENTIFIC CALCULATOR");
        System.out.println("       SPE Mini Project");
        System.out.println("=============================================");

        while (running) {
            System.out.println("\n--- Menu ---");
            System.out.println("2. Subtract\n3. Multiply\n4. Divide");
            System.out.println("5. Power\n6. Square Root\n7. Logarithm\n8. Factorial");
            System.out.println("9. Exit");
            System.out.print("Choose an option (1-9): ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number between 1 and 9.");
                continue;
            }

            try {
                switch (choice) {
                    // case 1:
                    //     System.out.print("Enter first number: ");
                    //     double a1 = Double.parseDouble(scanner.nextLine().trim());
                    //     System.out.print("Enter second number: ");
                    //     double b1 = Double.parseDouble(scanner.nextLine().trim());
                    //     System.out.printf("%.4f + %.4f = %.4f%n", a1, b1, add(a1, b1));
                    //     break;

                    case 2:
                        System.out.print("Enter first number: ");
                        double a2 = Double.parseDouble(scanner.nextLine().trim());
                        System.out.print("Enter second number: ");
                        double b2 = Double.parseDouble(scanner.nextLine().trim());
                        System.out.printf("%.4f - %.4f = %.4f%n", a2, b2, subtract(a2, b2));
                        break;

                    case 3:
                        System.out.print("Enter first number: ");
                        double a3 = Double.parseDouble(scanner.nextLine().trim());
                        System.out.print("Enter second number: ");
                        double b3 = Double.parseDouble(scanner.nextLine().trim());
                        System.out.printf("%.4f * %.4f = %.4f%n", a3, b3, multiply(a3, b3));
                        break;

                    case 4:
                        System.out.print("Enter first number: ");
                        double a4 = Double.parseDouble(scanner.nextLine().trim());
                        System.out.print("Enter second number: ");
                        double b4 = Double.parseDouble(scanner.nextLine().trim());
                        System.out.printf("%.4f / %.4f = %.4f%n", a4, b4, divide(a4, b4));
                        break;

                    case 5:
                        System.out.print("Enter the base (x): ");
                        double base = Double.parseDouble(scanner.nextLine().trim());
                        System.out.print("Enter the exponent (b): ");
                        double exponent = Double.parseDouble(scanner.nextLine().trim());
                        System.out.printf("%.4f ^ %.4f = %.4f%n", base, exponent, power(base, exponent));
                        break;

                    case 6:
                        System.out.print("Enter a number: ");
                        double sqrtInput = Double.parseDouble(scanner.nextLine().trim());
                        System.out.printf("√%.4f = %.4f%n", sqrtInput, squareRoot(sqrtInput));
                        break;

                    case 7:
                        System.out.print("Enter a positive number: ");
                        double logInput = Double.parseDouble(scanner.nextLine().trim());
                        System.out.printf("ln(%.4f) = %.4f%n", logInput, naturalLog(logInput));
                        break;

                    case 8:
                        System.out.print("Enter a non-negative integer: ");
                        int factInput = Integer.parseInt(scanner.nextLine().trim());
                        System.out.printf("%d! = %d%n", factInput, factorial(factInput));
                        break;

                    case 9:
                        running = false;
                        System.out.println("Exiting calculator. Goodbye!");
                        break;

                    default:
                        System.out.println("Invalid choice. Please select 1-9.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid number format. Please try again.");
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        scanner.close();
    }
}
