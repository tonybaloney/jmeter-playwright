package com.microsoft.playwright.jmeter.samplers

import com.microsoft.playwright.Page
import com.microsoft.playwright.TimeoutError
import com.microsoft.playwright.jmeter.PlaywrightSampleResult
import com.microsoft.playwright.jmeter.threadgroup.PlaywrightBrowserThreadGroup
import com.microsoft.playwright.options.WaitUntilState
import org.apache.jmeter.samplers.AbstractSampler
import org.apache.jmeter.samplers.Entry
import org.apache.jmeter.samplers.SampleResult
import org.apache.jmeter.threads.ThreadGroup
import org.slf4j.LoggerFactory

class PlaywrightSampler : AbstractSampler() {
    private val log = LoggerFactory.getLogger(ThreadGroup::class.java)

    var url: String?
        get() = getPropertyAsString(URL, "")
        set(label) {
            setProperty(URL, label)
        }

    var timeout: Int
        get() = getPropertyAsInt(
            TIMEOUT, 5000
        )
        set(timeout) {
            setProperty(TIMEOUT, timeout)
        }

    var waitUntilState: WaitUntilState
        get() = WaitUntilState.valueOf(getPropertyAsString(WAITUNTILSTATE, "LOAD"))
        set(value) {
            setProperty(WAITUNTILSTATE, value.toString())
        }

    var referer: String
        get() = getPropertyAsString(REFERER, "")
        set(label) {
            setProperty(REFERER, label)
        }


    override fun sample(entry: Entry?): SampleResult {
        val result = PlaywrightSampleResult()
        result.url = java.net.URL(url)
        result.sampleLabel = "Playwright Sample - $url"
        result.sampleStart()
        val options = Page.NavigateOptions()
        options.timeout = timeout.toDouble()
        options.waitUntil = waitUntilState
        if (referer != "")
            options.referer = referer

        log.debug("Sampling {} with Playwright, waiting for {}.", url, waitUntilState)

        if (this.threadContext.threadGroup is PlaywrightBrowserThreadGroup){
            val browser = (threadContext.threadGroup as PlaywrightBrowserThreadGroup).getBrowserContext(threadContext.thread.threadNum)

            if (browser == null){
                result.isSuccessful = false
                result.responseMessage = "No browser context"
            } else {
                val page: Page = browser.newPage()
                try {
                    val response = page.navigate(url, options)
                    result.page = page
                    result.isSuccessful = response.ok()
                    result.responseCode = response.status().toString()
                    result.responseHeaders = response.headersArray().joinToString("\n") { "${it.name}:${it.value}" }
                    result.title = page.title()
                } catch (_: TimeoutError) {
                    result.isSuccessful = false
                    result.errorCount += 1
                    result.responseMessage = "Timeout within playwright"
                }
            }
        } else {
            result.responseMessage = "Incorrectly configured, use inside a Playwright Thread Group."
            log.debug("Playwright sampler must be used inside a Playwright Thread Group.")
        }
        log.debug("Completed sample of $url with Playwright.")
        result.sampleEnd()
        return result
    }

    companion object {
        private const val URL = "Playwright.URL"
        private const val TIMEOUT = "Playwright.timeout"
        private const val WAITUNTILSTATE = "Playwright.waitUntilState"
        private const val REFERER = "Playwright.referer"
    }
}