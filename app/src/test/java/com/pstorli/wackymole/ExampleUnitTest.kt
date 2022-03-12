package com.pstorli.wackymole

import org.junit.Test

import org.junit.Assert.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
internal class ExampleUnitTest {
    val mockUserSession: UserSession = mockk()

    private fun mockk(): UserSession {

    }

    lateinit var dut: MyClass
    
    @BeforeEach
    fun setup() {

    }

    @AfterEach
    fun tearDown() {

    }

    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
}