package com.microsoft.playwright.jmeter.samplers

import com.microsoft.playwright.jmeter.junit.JUnitDeserializer
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class PlaywrightTestSampleResultTest {
    @Test
    fun `test complex case into suites`() {
        val testReport = this::class.java.classLoader.getResource("results-contosotraders-failures.xml")?.readText()
        assertNotNull(testReport)
        val testsuites = JUnitDeserializer().deserialize(testReport!!)
        val suite = PlaywrightTestSampleResult(testsuites, System.currentTimeMillis(), System.currentTimeMillis() + 1000)

        assertFalse(suite.isSuccessful)
        assertEquals(suite.errorCount, 0)

        // Build suites
        val testSuiteResult = PlaywrightTestSuiteSampleResult(testsuites.testsuite[0])
        assertTrue(testSuiteResult.isSuccessful)
        assertEquals(testSuiteResult.errorCount, 0)

        // Build case
        val testCaseResult = PlaywrightTestCaseSampleResult(testsuites.testsuite[0].testcase[0], testsuites.testsuite[0])
        assertTrue(testCaseResult.isSuccessful)

        val testCaseResultFalse = PlaywrightTestCaseSampleResult(testsuites.testsuite[3].testcase[0], testsuites.testsuite[3])
        assertFalse(testCaseResultFalse.isSuccessful)
        assertTrue(testCaseResultFalse.responseMessage.contains("Invalid URL"))
    }
}