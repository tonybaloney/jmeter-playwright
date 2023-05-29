package com.microsoft.playwright.jmeter.samplers

import com.microsoft.playwright.Page
import com.microsoft.playwright.Response
import com.microsoft.playwright.jmeter.PlaywrightSampleResult
import com.microsoft.playwright.jmeter.compiler.JavaStringCompiler
import com.microsoft.playwright.jmeter.compiler.RunnableJMeterSampler
import com.microsoft.playwright.jmeter.threadgroup.PlaywrightBrowserThreadGroup
import org.apache.jmeter.samplers.AbstractSampler
import org.apache.jmeter.samplers.Entry
import org.apache.jmeter.samplers.SampleResult
import org.apache.jmeter.threads.ThreadGroup
import org.opentest4j.AssertionFailedError
import org.slf4j.LoggerFactory

class PlaywrightScriptedSampler : AbstractSampler() {
    private val log = LoggerFactory.getLogger(ThreadGroup::class.java)

    var code: String
        get() = getPropertyAsString(CODE, "")
        set(value) {
            setProperty(CODE, value)
        }

    var className: String
        get() = getPropertyAsString(CLASSNAME, "")
        set(value) {
            setProperty(CLASSNAME, value)
        }

    var captureConsoleLogs: Boolean
        get() = getPropertyAsBoolean(CAPTURE_CONSOLE_LOGS, false)
        set(value) {
            setProperty(CAPTURE_CONSOLE_LOGS, value)
        }

    private var compiler = JavaStringCompiler()
    private var compiledSampler: RunnableJMeterSampler? = null

    override fun sample(entry: Entry?): SampleResult {
        if (this.threadContext.threadGroup is PlaywrightBrowserThreadGroup){
            val browser = (threadContext.threadGroup as PlaywrightBrowserThreadGroup).getBrowserContext(threadContext.thread.threadNum)
            val sampleResult = PlaywrightSampleResult()
            sampleResult.sampleLabel = name
            sampleResult.title = "No title"
            val pageHandler = { it: Page ->
                sampleResult.page = it
                it.onLoad {
                    sampleResult.title = it.title()
                }
                if (captureConsoleLogs) {
                    it.onConsoleMessage {
                        log.info("Console message from ${it.location()} - ${it.text()}")
                    }
                }
            }
            browser?.onPage(pageHandler)

            val responseHandler = { response: Response ->
                if (response.request().isNavigationRequest) {
                    sampleResult.isSuccessful = response.ok()
                    sampleResult.responseCode = response.status().toString()
                    sampleResult.responseHeaders = response.headersArray().joinToString("\n") { "${it.name}:${it.value}" }
                    sampleResult.responseData = response.body()
                }
            }
            browser?.onResponse(responseHandler)

            if (compiledSampler == null) {
                log.info("Compiling $className into scripted Playwright file")
                compiledSampler = compiler.compile(className, code)
            }
            sampleResult.sampleStart()
            try {
                compiledSampler?.run(browser)
                sampleResult.isSuccessful = true
                sampleResult.responseMessage = "Sampled scripted file $className"
            } catch (a: AssertionFailedError){
                sampleResult.isSuccessful = false
                sampleResult.errorCount += 1
                sampleResult.responseMessage += a.message
            } catch (e: Exception) {
                sampleResult.isSuccessful = false
                sampleResult.errorCount += 1
                sampleResult.responseMessage = e.message
            } finally {
                sampleResult.sampleEnd()
            }
            browser?.offPage(pageHandler)
            browser?.offResponse(responseHandler)
            return sampleResult
        } else {
            val result = PlaywrightSampleResult()
            result.responseMessage = "Incorrectly configured, use inside a Playwright Thread Group."
            log.debug("Playwright sampler must be used inside a Playwright Thread Group.")
            return result
        }
    }

    companion object {
        private const val CLASSNAME = "Playwright.classname"
        private const val CODE = "Playwright.code"
        private const val CAPTURE_CONSOLE_LOGS = "Playwright.captureConsoleLogs"
    }
}