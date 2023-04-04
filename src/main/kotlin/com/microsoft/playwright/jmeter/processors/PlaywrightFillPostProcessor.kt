package com.microsoft.playwright.jmeter.processors

import com.microsoft.playwright.jmeter.PlaywrightSampleResult
import org.apache.jmeter.threads.ThreadGroup
import org.slf4j.LoggerFactory

class PlaywrightFillPostProcessor: AbstractSelectableProcessor() {
    private val log = LoggerFactory.getLogger(ThreadGroup::class.java)

    var fillInput: String
        get() = getPropertyAsString(FILL_INPUT, "")
        set(label) {
            setProperty(FILL_INPUT, label)
        }

    override fun process() {
        if (this.threadContext.previousResult is PlaywrightSampleResult) {
            val result = this.threadContext.previousResult as PlaywrightSampleResult
            if (result.page != null) {
                select(selectorType, result.page!!, selectorInput).fill(fillInput)
                log.info("Filled element $selectorInput ($selectorType) with value $fillInput.")
            }
        } else {
            log.info("Last result is not a Playwright result")
        }
    }

    companion object {
        const val FILL_INPUT = "Playwright.fillInput"
    }
}