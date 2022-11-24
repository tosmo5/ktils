package com.tosmo.ktils.calc

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

internal class SegmentationCalculatorTest {

    private val mCalc = SegmentationCalculator()

    @BeforeEach
    fun setUp() {
        mCalc.add(36000, 3)
        mCalc.add(144000, 10)
        mCalc.add(300000, 20)
    }

    @AfterEach
    fun tearDown() {
        mCalc.clear()
    }

    @Test
    fun add() {
        assertThrows<IllegalArgumentException> {
            mCalc.add(250000, 5)
        }
    }

    @Test
    fun calc() {
        assertEquals(0, mCalc.calc(300000).compareTo(43080.0.toBigDecimal()))
        assertEquals(0, mCalc.calc(350000).compareTo(43080.0.toBigDecimal()))
    }
}