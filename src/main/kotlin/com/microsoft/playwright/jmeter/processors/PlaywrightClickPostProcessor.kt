package com.microsoft.playwright.jmeter.processors

import com.microsoft.playwright.jmeter.PlaywrightSampleResult
import com.microsoft.playwright.jmeter.selectAsLocator
import org.apache.jmeter.threads.ThreadGroup
import org.slf4j.LoggerFactory

class PlaywrightClickPostProcessor: AbstractSelectableProcessor() {
    private val log = LoggerFactory.getLogger(ThreadGroup::class.java)

    override fun process() {
        if (this.threadContext.previousResult is PlaywrightSampleResult) {
            val result = this.threadContext.previousResult as PlaywrightSampleResult
            if (result.page != null) {
                selectAsLocator(selectorType, result.page!!, selectorInput).click()
                log.info("Clicked element $selectorInput ($selectorType), URL is now ${result.page?.url()}.")
            }
        } else {
            log.info("Last result is not a Playwright result")
        }
    }
}