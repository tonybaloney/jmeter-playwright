package com.microsoft.playwright.jmeter.processors

import com.microsoft.playwright.Locator
import com.microsoft.playwright.jmeter.PlaywrightSampleResult
import com.microsoft.playwright.jmeter.threadgroup.PlaywrightBrowserThreadGroup
import org.apache.jmeter.processor.PostProcessor
import org.apache.jmeter.testelement.AbstractTestElement
import org.apache.jmeter.threads.ThreadGroup
import org.slf4j.LoggerFactory

enum class SelectorType {
    Title,
    XPath,
    Text
}

class PlaywrightClickPostProcessor: PostProcessor, AbstractTestElement() {
    private val log = LoggerFactory.getLogger(ThreadGroup::class.java)

    var selectorType: SelectorType
        get() = SelectorType.valueOf(getPropertyAsString(SELECTOR_TYPE, "Title"))
        set(label) {
            setProperty(SELECTOR_TYPE, label.toString())
        }
    var selectorInput: String?
        get() = getPropertyAsString(SELECTOR, "")
        set(label) {
            setProperty(SELECTOR, label)
        }

    override fun process() {
        if (this.threadContext.previousResult is PlaywrightSampleResult) {
            val result = this.threadContext.previousResult as PlaywrightSampleResult
            val locator: Locator? = when (selectorType) {
                SelectorType.Title -> {
                    result.page?.getByTitle(selectorInput)
                }

                SelectorType.XPath -> {
                    result.page?.locator(selectorInput)
                }

                SelectorType.Text -> {
                    result.page?.getByText(selectorInput)
                }
            }
            locator?.click()
            log.info("Clicked element ${selectorInput} ($selectorType), URL is now ${result.page?.url()}.")
        } else {
            log.info("Last result is not a Playwright result")
        }
    }

    companion object {
        private const val SELECTOR = "Playwright.selector"
        private const val SELECTOR_TYPE = "Playwright.selectorType"
    }
}