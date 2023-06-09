package com.microsoft.playwright.jmeter.junit

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class DeserializerTest {
    val test1 = """
        <testsuites id="" name="" tests="2" failures="0" skipped="0" errors="0" time="6.816886000037194">
        <testsuite name="example.spec.ts" timestamp="2023-05-30T04:12:04.486Z" hostname="" tests="2" failures="0" skipped="0" time="5.95" errors="0">
        <testcase name="has title" classname="example.spec.ts" time="3.102">
        </testcase>
        <testcase name="get started link" classname="example.spec.ts" time="2.848">
        </testcase>
        </testsuite>
        </testsuites>
    """.trimIndent()

    @Test
    fun `test simple test suites`() {
        val testsuites = JUnitDeserializer().deserialize(test1)
        assertEquals(testsuites.name, "")
        assertEquals(testsuites.tests, 2)
        assertEquals(testsuites.failures, 0)
        assertEquals(testsuites.skipped, 0)
        assertEquals(testsuites.errors, 0)
        assertEquals(testsuites.time, 6.816886000037194)

        assertEquals(testsuites.testsuite.size, 1)
        assertEquals(testsuites.testsuite[0].name, "example.spec.ts")
        assertEquals(testsuites.testsuite[0].time, 5.95)
        assertEquals(testsuites.testsuite[0].tests, 2)
        assertEquals(testsuites.testsuite[0].failures, 0)
        assertEquals(testsuites.testsuite[0].skipped, 0)
        assertEquals(testsuites.testsuite[0].errors, 0)
        assertEquals(testsuites.testsuite[0].testcase.size, 2)
        assertEquals(testsuites.testsuite[0].timestamp, "2023-05-30T04:12:04.486Z")

        assertEquals(testsuites.testsuite[0].testcase[0].name, "has title")
        assertEquals(testsuites.testsuite[0].testcase[0].classname, "example.spec.ts")
        assertEquals(testsuites.testsuite[0].testcase[0].time, 3.102)
        assertEquals(testsuites.testsuite[0].testcase[1].name, "get started link")
        assertEquals(testsuites.testsuite[0].testcase[1].classname, "example.spec.ts")
        assertEquals(testsuites.testsuite[0].testcase[1].time, 2.848)
    }

    @Test
    fun `test complex report`() {
        val testReport = this::class.java.classLoader.getResource("results-contosotraders-failures.xml")?.readText()
        assertNotNull(testReport)
        val testsuites = JUnitDeserializer().deserialize(testReport!!)

        assertEquals(testsuites.tests, 41)
        assertEquals(testsuites.failures, 27)
        assertEquals(testsuites.skipped, 14)
        assertEquals(testsuites.disabled, 0)
        assertEquals(testsuites.time, 73.2930759999752)

        assertEquals(testsuites.testsuite.size, 10)
        assertEquals(testsuites.testsuite[0].name, "api/cart.spec.ts")
        assertEquals(testsuites.testsuite[0].testcase.size, 1)
        assertEquals(testsuites.testsuite[0].tests, 1)
        assertEquals(testsuites.testsuite[0].testcase[0].name, "Shopping Cart API › should be able to GET shopping cart")
    }

    @Test
    fun `test mixed results report`() {
        val testReport = this::class.java.classLoader.getResource("results_mixed.xml")?.readText()
        assertNotNull(testReport)
        val testsuites = JUnitDeserializer().deserialize(testReport!!)

        assertEquals(testsuites.tests, 68)
        assertEquals(testsuites.failures, 26)
        assertEquals(testsuites.skipped, 11)
        assertEquals(testsuites.disabled, 0)
        assertTrue(testsuites.testsuite[2].testcase[0].systemOut[0].content.contains("test-failed-1.png"))
    }

    @Test
    fun `test deserialize pytest results`() {
        val testReport = this::class.java.classLoader.getResource("results_pytest.xml")?.readText()
        assertNotNull(testReport)
        val testsuites = JUnitDeserializer().deserialize(testReport!!)

        assertEquals(testsuites.testsuite[0].tests, 158)
    }

    @Test
    fun `test deserialize junit results`() {
        val testReport = this::class.java.classLoader.getResource("results_junit5-1.xml")?.readText()
        assertNotNull(testReport)
        val testsuites = JUnitDeserializer().deserialize(testReport!!)

        assertEquals(testsuites.testsuite[0].tests, 4)

        val testReport2 = this::class.java.classLoader.getResource("results_junit5-2.xml")?.readText()
        assertNotNull(testReport2)
        val testsuites2 = JUnitDeserializer().deserialize(testReport2!!)

        assertEquals(testsuites2.testsuite[0].tests, 1)
    }
}