package com.microsoft.playwright.jmeter.samplers

import com.microsoft.playwright.jmeter.junit.JUnitDeserializer
import com.microsoft.playwright.jmeter.BrowserType
import org.apache.jmeter.samplers.AbstractSampler
import org.apache.jmeter.samplers.Entry
import org.apache.jmeter.samplers.SampleResult
import org.apache.jmeter.threads.ThreadGroup
import org.slf4j.LoggerFactory
import java.io.File
import java.io.IOException


class PlaywrightTestSampler : AbstractSampler() {
    private var proc: Process? = null
    private val log = LoggerFactory.getLogger(ThreadGroup::class.java)

    var browserType: BrowserType
        get() = BrowserType.valueOf(getPropertyAsString(BROWSER, "Chromium"))
        set(value) {
            setProperty(BROWSER, value.toString())
        }

    var testDirectory: File
        get() = File(getPropertyAsString(TEST_DIRECTORY, ""))
        set(value) {
            setProperty(TEST_DIRECTORY, value.toString())
        }

    override fun sample(entry: Entry?): SampleResult {
        val sampleStartTime = System.currentTimeMillis()
        runNpxPlaywrightTest(testDirectory)
        val sampleEndTime = System.currentTimeMillis()
        val result = proc?.inputStream?.bufferedReader()?.readText() ?: ""
        val errorResult = proc?.errorStream?.bufferedReader()?.readText() ?: ""
        if (errorResult != "")
            log.error(errorResult)

        val testsuites = JUnitDeserializer().deserialize(result)
        log.info("Ran ${testsuites.tests} tests in ${testsuites.time} seconds in ${testsuites.name}.")

        if (testsuites.testsuite.size > 1 ) {
            val sampleResult = PlaywrightTestSampleResult(testsuites, sampleStartTime, sampleEndTime)

            for (suite in testsuites.testsuite) {
                val suiteResult = PlaywrightTestSuiteSampleResult(suite)
                for (case in suite.testcase) {
                    suiteResult.addSubResult(PlaywrightTestCaseSampleResult(case, suite), false)
                }
                sampleResult.addSubResult(suiteResult, false)
            }
            return sampleResult
        } else if (testsuites.testsuite.size  == 1) {
            val suiteResult = PlaywrightTestSuiteSampleResult(testsuites.testsuite[0])
            for (case in testsuites.testsuite[0].testcase) {
                suiteResult.addSubResult(PlaywrightTestCaseSampleResult(case, testsuites.testsuite[0]), false)
            }
            return suiteResult
        } else {
            return SampleResult()
        }
    }

    private fun runNpxPlaywrightTest(workingDir: File) {
        try {
            val parts = arrayListOf("npx", "playwright", "test", "--reporter", "junit")
            log.info("Launching process $parts in directory ${workingDir}.")
            this.proc = Runtime.getRuntime().exec(parts.joinToString(" ") , null, workingDir)
        } catch(e: IOException) {
            log.error(e.message)
            log.error(e.stackTraceToString())
        }
    }

    companion object {
        private const val BROWSER = "Playwright.browser"
        private const val TEST_DIRECTORY = "Playwright.testDirectory"
    }
}