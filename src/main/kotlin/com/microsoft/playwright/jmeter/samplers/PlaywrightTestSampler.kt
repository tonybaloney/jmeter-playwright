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

    var testDirectory: File
        get() = File(getPropertyAsString(TEST_DIRECTORY, ""))
        set(value) {
            setProperty(TEST_DIRECTORY, value.toString())
        }

    var extraOptions: String
        get() = getPropertyAsString(EXTRA_OPTIONS, "")
        set(value) {
            setProperty(EXTRA_OPTIONS, value)
        }

    var configFile: File
        get() = File(getPropertyAsString(CONFIG_FILE, ""))
        set(value) {
            setProperty(CONFIG_FILE, value.toString())
        }

    var workerCount: Int
        get() = getPropertyAsInt(WORKER_COUNT, 0)
        set(value) {
            setProperty(WORKER_COUNT, value)
        }

    var repeatEach: Int
        get() = getPropertyAsInt(REPEAT_EACH, 0)
        set(value) {
            setProperty(REPEAT_EACH, value)
        }

    var timeout: Int
        get() = getPropertyAsInt(TIMEOUT, 30000)
        set(value) {
            setProperty(TIMEOUT, value)
        }

    var grep: String
        get() = getPropertyAsString(GREP, "")
        set(value) {
            setProperty(GREP, value)
        }

    var grepInvert: String
        get() = getPropertyAsString(GREP_INVERT, "")
        set(value) {
            setProperty(GREP_INVERT, value)
        }

    var project: String
        get() = getPropertyAsString(PROJECT, "")
        set(value) {
            setProperty(PROJECT, value)
        }

    override fun sample(entry: Entry?): SampleResult {
        val sampleStartTime = System.currentTimeMillis()
        runNpxPlaywrightTest(testDirectory, extraOptions)
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
            return PlaywrightTestEmptySampleResult(sampleStartTime, sampleEndTime)
        }
    }

    private fun runNpxPlaywrightTest(workingDir: File, extraOptions: String) {
        try {
            val parts = arrayListOf("npx", "playwright", "test", "--reporter", "junit")

            if (workerCount != 0){
                parts.add("--workers=$workerCount")
            }
            if (repeatEach != 0) {
                parts.add("--repeat-each=$repeatEach")
            }
            parts.add("--timeout=$timeout")
            if (grep.isNotEmpty()) {
                parts.add("--grep")
                parts.add(grep)
            }
            if (grepInvert.isNotEmpty()) {
                parts.add("--grep-invert")
                parts.add(grepInvert)
            }
            if (configFile.exists()){
                parts.add("--config=${configFile}")
            }
            if (project.isNotEmpty()){
                parts.add("--project")
                parts.add(project)
            }

            if (extraOptions.isNotEmpty())
                parts.add(extraOptions)

            log.info("Launching ${parts.joinToString(" ")} in directory ${workingDir}.")
            this.proc = Runtime.getRuntime().exec(parts.toTypedArray(), null, workingDir)
        } catch(e: IOException) {
            log.error(e.message)
            log.error(e.stackTraceToString())
        }
    }

    companion object {
        private const val TEST_DIRECTORY = "Playwright.testDirectory"
        private const val EXTRA_OPTIONS = "Playwright.extraOptions"
        private const val CONFIG_FILE = "Playwright.configFile"
        private const val WORKER_COUNT = "Playwright.workerCount"
        private const val REPEAT_EACH = "Playwright.repeatEach"
        private const val TIMEOUT = "Playwright.timeout"
        private const val GREP = "Playwright.grep"
        private const val GREP_INVERT = "Playwright.grepInvert"
        private const val PROJECT = "Playwright.project"
    }
}
