package com.example.arithmetic;


import com.example.cov.ArithmeticOperations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class ArithmeticOperationsTest {

    private final ArithmeticOperations operations = new ArithmeticOperations();

    @Test
    public void testAddition() {
        assertEquals(5, operations.add(2, 3));
        assertEquals(-1, operations.add(-2, 1));
    }
    
    @Test
    public void testSubtraction() {
        assertEquals(1, operations.subtract(3, 2));
        assertEquals(-3, operations.subtract(-2, 1));
    }

    @Test
    public void testMultiplication() {
        assertEquals(6, operations.multiply(2, 3));
        assertEquals(-2, operations.multiply(-2, 1));
    }

    @Test
    public void testDivision() {
        assertEquals(2, operations.divide(6, 3));
        //assertEquals(-2, operations.divide(-6, 3));
        assertThrows(IllegalArgumentException.class, () -> operations.divide(1, 0));
    }
}

