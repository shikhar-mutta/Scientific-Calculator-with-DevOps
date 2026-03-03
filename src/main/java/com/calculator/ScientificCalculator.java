package com.calculator;

import java.util.Scanner;

/**
 * Scientific Calculator - SPE Mini Project
 * 
 * A menu-driven scientific calculator supporting:
 * 1. Square root - √x
 * 2. Factorial - x!
 * 3. Natural log - ln(x)
 * 4. Power - x^b
 */
public class ScientificCalculator {

    // ───────────────────────── Core Operations ─────────────────────────

    /**Monitoring using ELK Stack (Proper Version)
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
     * Computes x raised to the power b.
     *
     * @param x the base
     * @param b the exponent
     * @return x^b
     */
    public static double power(double x, double b) {
        return Math.pow(x, b);
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
            System.out.println("1. Square Root   (√x)");
            System.out.println("2. Factorial     (x!)");
            System.out.println("3. Natural Log   (ln x)");
            System.out.println("4. Power         (x^b)");
            System.out.println("5. Exit");
            System.out.print("Choose an option (1-5): ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number between 1 and 5.");
                continue;
            }

            try {
                switch (choice) {
                    case 1:
                        System.out.print("Enter a number: ");
                        double sqrtInput = Double.parseDouble(scanner.nextLine().trim());
                        double sqrtResult = squareRoot(sqrtInput);
                        System.out.printf("√%.4f = %.4f%n", sqrtInput, sqrtResult);
                        break;

                    case 2:
                        System.out.print("Enter a non-negative integer: ");
                        int factInput = Integer.parseInt(scanner.nextLine().trim());
                        long factResult = factorial(factInput);
                        System.out.printf("%d! = %d%n", factInput, factResult);
                        break;

                    case 3:
                        System.out.print("Enter a positive number: ");
                        double logInput = Double.parseDouble(scanner.nextLine().trim());
                        double logResult = naturalLog(logInput);
                        System.out.printf("ln(%.4f) = %.4f%n", logInput, logResult);
                        break;

                    case 4:
                        System.out.print("Enter the base (x): ");
                        double base = Double.parseDouble(scanner.nextLine().trim());
                        System.out.print("Enter the exponent (b): ");
                        double exponent = Double.parseDouble(scanner.nextLine().trim());
                        double powResult = power(base, exponent);
                        System.out.printf("%.4f ^ %.4f = %.4f%n", base, exponent, powResult);
                        break;

                    case 5:
                        running = false;
                        System.out.println("Exiting calculator. Goodbye!");
                        break;

                    default:
                        System.out.println("Invalid choice. Please select 1-5.");
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
