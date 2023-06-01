package com.microsoft.playwright.jmeter.junit

import org.junit.jupiter.api.Assertions.assertEquals
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
    fun `test simple test suites`(){
        val testsuites = JUnitDeserializer().deserialize(test1)
        assertEquals(testsuites.name, "")
        assertEquals(testsuites.tests, 2)
        assertEquals(testsuites.failures, 0)
        assertEquals(testsuites.skipped, 0)
        assertEquals(testsuites.errors, 0)
        assertEquals(testsuites.time, "6.816886000037194")

        assertEquals(testsuites.testsuite.size, 1)
        assertEquals(testsuites.testsuite[0].name, "example.spec.ts")
        assertEquals(testsuites.testsuite[0].time, "5.95")
        assertEquals(testsuites.testsuite[0].tests, 2)
        assertEquals(testsuites.testsuite[0].failures, 0)
        assertEquals(testsuites.testsuite[0].skipped, 0)
        assertEquals(testsuites.testsuite[0].errors, 0)
        assertEquals(testsuites.testsuite[0].testcase.size, 2)
        assertEquals(testsuites.testsuite[0].timestamp, "2023-05-30T04:12:04.486Z")

        assertEquals(testsuites.testsuite[0].testcase[0].name, "has title")
        assertEquals(testsuites.testsuite[0].testcase[0].classname, "example.spec.ts")
        assertEquals(testsuites.testsuite[0].testcase[0].time, "3.102")
        assertEquals(testsuites.testsuite[0].testcase[1].name, "get started link")
        assertEquals(testsuites.testsuite[0].testcase[1].classname, "example.spec.ts")
        assertEquals(testsuites.testsuite[0].testcase[1].time, "2.848")
    }
}