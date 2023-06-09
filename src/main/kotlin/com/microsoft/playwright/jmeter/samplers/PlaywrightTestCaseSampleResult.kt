package com.microsoft.playwright.jmeter.samplers

import com.microsoft.playwright.jmeter.junit.testcase
import com.microsoft.playwright.jmeter.junit.testsuite
import com.microsoft.playwright.jmeter.junit.testsuites
import org.apache.jmeter.samplers.SampleResult
import java.time.ZonedDateTime
import kotlin.math.roundToLong

class PlaywrightTestSampleResult(var testsuites: testsuites, startedAt: Long, finishedAt: Long) : SampleResult() {
    init {
        startTime = startedAt
        endTime = finishedAt
        isSuccessful = testsuites.errors == 0 && testsuites.failures == 0
    }

    override fun toString(): String {
        return "Playwright test run"
    }

    override fun getTime(): Long {
        return (testsuites.time * 1000).roundToLong()
    }

    override fun getErrorCount(): Int {
        return testsuites.errors
    }
}
class PlaywrightTestCaseSampleResult(var testcase: testcase, testsuite: testsuite) : SampleResult() {
    init {
        startTime = ZonedDateTime.parse(testsuite.timestamp).toInstant().toEpochMilli()
        isSuccessful = testcase.error.isEmpty() && testcase.failure.isEmpty()
    }
    override fun toString(): String {
        return testcase.name
    }

    override fun getTime(): Long {
        return (testcase.time * 1000).roundToLong()
    }

    override fun getResponseMessage(): String {
        if (testcase.failure.isEmpty()) return ""
        return testcase.failure.first().content
    }
}

class PlaywrightTestSuiteSampleResult(var testsuite: testsuite) : SampleResult() {
    init {
        startTime = ZonedDateTime.parse(testsuite.timestamp).toInstant().toEpochMilli()
        isSuccessful = testsuite.errors == 0 && testsuite.failures == 0
    }
    override fun toString(): String {
        return testsuite.name
    }

    override fun getTime(): Long {
        return (testsuite.time * 1000).roundToLong()
    }
}