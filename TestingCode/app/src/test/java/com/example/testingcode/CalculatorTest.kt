package com.example.testingcode


import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class CalculatorTest {

    // Create instance of class under test
    private val calculator = Calculator()

    @Test
    fun testAddition_isCorrect() {
        val result = calculator.add(2, 3)
        assertEquals(5, result)  // ✅ pass
    }

    @Test
    fun testSubtraction_isCorrect() {
        val result = calculator.subtract(5, 2)
        assertEquals(3, result)
    }

    @Test
    fun testDivision_isCorrect() {
        val result = calculator.divide(10, 2)
        assertEquals(5, result)
    }

    @Test
    fun testDivision_byZero_shouldThrowException() {
        assertThrows(IllegalArgumentException::class.java) {
            calculator.divide(10, 0)
        }
    }
}
