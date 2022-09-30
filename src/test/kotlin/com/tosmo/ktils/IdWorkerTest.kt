package com.tosmo.ktils

import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test

internal class IdWorkerTest {

    private var mIdWorker = IdWorker()

    @Test
    fun nextId() {
        repeat(10) {
            val id0 = mIdWorker.nextId()
            val id1 = mIdWorker.nextId()
            assertNotEquals(id0, id1)
        }
    }
}