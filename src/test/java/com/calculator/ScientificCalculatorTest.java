package com.calculator;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the ScientificCalculator class.
 */
public class ScientificCalculatorTest {

    // ─────────────────── Addition Tests ──────────────────────

    // @Test
    // public void testAddPositive() {
    //     assertEquals(7.0, ScientificCalculator.add(3, 4), 1e-9);
    // }

    // @Test
    // public void testAddNegative() {
    //     assertEquals(-3.0, ScientificCalculator.add(-1, -2), 1e-9);
    // }

    // @Test
    // public void testAddZero() {
    //     assertEquals(5.0, ScientificCalculator.add(5, 0), 1e-9);
    // }

    // @Test
    // public void testAddDecimals() {
    //     assertEquals(3.3, ScientificCalculator.add(1.1, 2.2), 1e-9);
    // }

    // ─────────────────── Subtraction Tests ───────────────────

    @Test
    public void testSubtractPositive() {
        assertEquals(2.0, ScientificCalculator.subtract(5, 3), 1e-9);
    }

    @Test
    public void testSubtractNegativeResult() {
        assertEquals(-2.0, ScientificCalculator.subtract(3, 5), 1e-9);
    }

    @Test
    public void testSubtractZero() {
        assertEquals(5.0, ScientificCalculator.subtract(5, 0), 1e-9);
    }

    @Test
    public void testSubtractSameNumbers() {
        assertEquals(0.0, ScientificCalculator.subtract(7, 7), 1e-9);
    }

    // ─────────────────── Multiplication Tests ────────────────

    @Test
    public void testMultiplyPositive() {
        assertEquals(15.0, ScientificCalculator.multiply(3, 5), 1e-9);
    }

    @Test
    public void testMultiplyByZero() {
        assertEquals(0.0, ScientificCalculator.multiply(5, 0), 1e-9);
    }

    @Test
    public void testMultiplyNegatives() {
        assertEquals(6.0, ScientificCalculator.multiply(-2, -3), 1e-9);
    }

    @Test
    public void testMultiplyMixed() {
        assertEquals(-10.0, ScientificCalculator.multiply(2, -5), 1e-9);
    }

    // ─────────────────── Division Tests ──────────────────────

    @Test
    public void testDividePositive() {
        assertEquals(2.5, ScientificCalculator.divide(5, 2), 1e-9);
    }

    @Test
    public void testDivideNegative() {
        assertEquals(-2.0, ScientificCalculator.divide(6, -3), 1e-9);
    }

    @Test
    public void testDivideZeroNumerator() {
        assertEquals(0.0, ScientificCalculator.divide(0, 5), 1e-9);
    }

    @Test
    public void testDivideByZeroThrows() {
        assertThrows(IllegalArgumentException.class, () -> ScientificCalculator.divide(5, 0));
    }

    // ─────────────────── Power Function Tests ────────────────

    @Test
    public void testPowerPositive() {
        assertEquals(8.0, ScientificCalculator.power(2, 3), 1e-9);
    }

    @Test
    public void testPowerZeroExponent() {
        assertEquals(1.0, ScientificCalculator.power(5, 0), 1e-9);
    }

    @Test
    public void testPowerNegativeExponent() {
        assertEquals(0.25, ScientificCalculator.power(2, -2), 1e-9);
    }

    @Test
    public void testPowerFractionalExponent() {
        assertEquals(2.0, ScientificCalculator.power(4, 0.5), 1e-9);
    }

    @Test
    public void testPowerBaseZero() {
        assertEquals(0.0, ScientificCalculator.power(0, 5), 1e-9);
    }

    // ─────────────────── Square Root Tests ───────────────────

    @Test
    public void testSquareRootPositive() {
        assertEquals(5.0, ScientificCalculator.squareRoot(25), 1e-9);
    }

    @Test
    public void testSquareRootZero() {
        assertEquals(0.0, ScientificCalculator.squareRoot(0), 1e-9);
    }

    @Test
    public void testSquareRootDecimal() {
        assertEquals(1.4142135623730951, ScientificCalculator.squareRoot(2), 1e-9);
    }

    @Test
    public void testSquareRootNegativeThrows() {
        assertThrows(IllegalArgumentException.class, () -> ScientificCalculator.squareRoot(-4));
    }

    // ─────────────────── Natural Log Tests ───────────────────

    @Test
    public void testNaturalLogOne() {
        assertEquals(0.0, ScientificCalculator.naturalLog(1), 1e-9);
    }

    @Test
    public void testNaturalLogE() {
        assertEquals(1.0, ScientificCalculator.naturalLog(Math.E), 1e-9);
    }

    @Test
    public void testNaturalLogPositive() {
        assertEquals(Math.log(10), ScientificCalculator.naturalLog(10), 1e-9);
    }

    @Test
    public void testNaturalLogZeroThrows() {
        assertThrows(IllegalArgumentException.class, () -> ScientificCalculator.naturalLog(0));
    }

    @Test
    public void testNaturalLogNegativeThrows() {
        assertThrows(IllegalArgumentException.class, () -> ScientificCalculator.naturalLog(-5));
    }

    // ─────────────────── Factorial Tests ─────────────────────

    @Test
    public void testFactorialZero() {
        assertEquals(1, ScientificCalculator.factorial(0));
    }

    @Test
    public void testFactorialOne() {
        assertEquals(1, ScientificCalculator.factorial(1));
    }

    @Test
    public void testFactorialFive() {
        assertEquals(120, ScientificCalculator.factorial(5));
    }

    @Test
    public void testFactorialTen() {
        assertEquals(3628800, ScientificCalculator.factorial(10));
    }

    @Test
    public void testFactorialTwenty() {
        assertEquals(2432902008176640000L, ScientificCalculator.factorial(20));
    }

    @Test
    public void testFactorialNegativeThrows() {
        assertThrows(IllegalArgumentException.class, () -> ScientificCalculator.factorial(-1));
    }

    @Test
    public void testFactorialTooLargeThrows() {
        assertThrows(IllegalArgumentException.class, () -> ScientificCalculator.factorial(21));
    }
}
