package com.microsoft.playwright.jmeter

import com.microsoft.playwright.Page
import com.microsoft.playwright.TimeoutError
import com.microsoft.playwright.options.WaitUntilState
import org.apache.jmeter.samplers.AbstractSampler
import org.apache.jmeter.samplers.Entry
import org.apache.jmeter.samplers.SampleResult
import org.apache.jmeter.threads.ThreadGroup
import org.slf4j.LoggerFactory

fun waitUntilStateToString(state: WaitUntilState) : String{
    return if (state == WaitUntilState.COMMIT){
        "COMMIT"
    } else if (state == WaitUntilState.DOMCONTENTLOADED) {
        "DOMCONTENTLOADED"
    } else if (state == WaitUntilState.LOAD){
        "LOAD"
    } else if (state == WaitUntilState.NETWORKIDLE) {
        "NETWORKIDLE"
    } else {
        "LOAD"
    }
}

fun waitUntilStateFromString(state: String) : WaitUntilState {
    return if (state.uppercase() == "COMMIT"){
        WaitUntilState.COMMIT
    } else if (state.uppercase() == "DOMCONTENTLOADED") {
        WaitUntilState.DOMCONTENTLOADED
    } else if (state.uppercase() == "LOAD"){
        WaitUntilState.LOAD
    } else if (state.uppercase() == "NETWORKIDLE") {
        WaitUntilState.NETWORKIDLE
    } else {
        WaitUntilState.LOAD
    }
}

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
        get() = waitUntilStateFromString(getPropertyAsString(WAITUNTILSTATE, "Chromium"))
        set(value: WaitUntilState) {
            setProperty(WAITUNTILSTATE, waitUntilStateToString(value))
        }

    var referer: String
        get() = getPropertyAsString(REFERER, "")
        set(label) {
            setProperty(REFERER, label)
        }


    override fun sample(entry: Entry?): SampleResult {
        val result = SampleResult()
        result.url = java.net.URL(url)
        result.sampleStart()
        val options = Page.NavigateOptions()
        options.timeout = timeout.toDouble()
        options.waitUntil = waitUntilState
        if (referer != "")
            options.referer = referer

        log.info("Sampling $url with Playwright.")

        if (this.threadContext.threadGroup is PlaywrightBrowserThreadGroup){
            val browser = (threadContext.threadGroup as PlaywrightBrowserThreadGroup).browser

            val page: Page = browser.newPage()
            try {
                page.navigate(url, options)
                log.info("Page title is ${page.title()}")
                result.isSuccessful = true
            } catch (_ : TimeoutError) {
                result.isSuccessful = false
                result.errorCount += 1
            }
        }
        log.info("Completed sample of $url with Playwright.")
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