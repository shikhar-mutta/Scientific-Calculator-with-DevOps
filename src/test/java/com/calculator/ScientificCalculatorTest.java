package com.calculator;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the ScientificCalculator class.
 */
public class ScientificCalculatorTest {

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
}
