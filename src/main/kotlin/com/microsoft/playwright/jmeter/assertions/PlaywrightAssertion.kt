package com.microsoft.playwright.jmeter.assertions

import com.microsoft.playwright.Locator
import com.microsoft.playwright.Page
import com.microsoft.playwright.jmeter.PlaywrightSampleResult
import com.microsoft.playwright.jmeter.assertions.AssertionType.*
import com.microsoft.playwright.jmeter.SelectorType
import com.microsoft.playwright.jmeter.selectAsLocator
import org.apache.jmeter.assertions.Assertion
import org.apache.jmeter.assertions.AssertionResult
import org.apache.jmeter.samplers.SampleResult
import org.apache.jmeter.testelement.AbstractTestElement
import org.apache.jmeter.threads.ThreadGroup
import org.slf4j.LoggerFactory
import java.nio.file.Paths

enum class AssertionType {
    IsChecked,
    IsDisabled,
    IsEditable,
    IsEnabled,
    IsHidden,
    IsVisible
}

class PlaywrightAssertion : Assertion, AbstractTestElement() {
    private val log = LoggerFactory.getLogger(ThreadGroup::class.java)

    var selectorType: SelectorType
        get() = SelectorType.valueOf(getPropertyAsString(SELECTOR_TYPE, "Title"))
        set(label) {
            setProperty(SELECTOR_TYPE, label.toString())
        }
    var selectorInput: String
        get() = getPropertyAsString(SELECTOR, "")
        set(label) {
            setProperty(SELECTOR, label)
        }

    var takeScreenshotOnFailure: Boolean
        get() = getPropertyAsBoolean(SCREENSHOT_FAILURES, false)
        set(v){
            setProperty(SCREENSHOT_FAILURES, v)
        }

    var assertionType: AssertionType
        get() = AssertionType.valueOf(getPropertyAsString(ASSERTION_TYPE, "IsVisible"))
        set(label) {
            setProperty(ASSERTION_TYPE, label.toString())
        }
    override fun getResult(response: SampleResult?): AssertionResult {
        val result = AssertionResult(this.name)
        if (response is PlaywrightSampleResult){
            if (response.page == null){
                result.isFailure = true
                result.isError = true
                result.failureMessage = "Response was not successful, cannot complete assertion."
                log.warn("Response is not a playwright sample result.")
            } else {
                result.isFailure = false
                // Assert
                val locator: Locator = selectAsLocator(selectorType, response.page!!, selectorInput)
                val assertionResult = when(assertionType) {
                    IsChecked -> locator.isChecked
                    IsDisabled -> locator.isDisabled
                    IsEditable -> locator.isEditable
                    IsEnabled -> locator.isEnabled
                    IsHidden -> locator.isHidden
                    IsVisible -> locator.isVisible
                }
                result.isFailure = assertionResult != true
                if (result.isFailure){
                    result.failureMessage = "Assertion $name failed with condition $assertionType"
                    if (takeScreenshotOnFailure) {
                        val screenshotPath = Paths.get("screenshot-${name}-assertion-failure.png")
                        response.page?.screenshot(Page.ScreenshotOptions().setPath(screenshotPath))
                        log.info("Saved screenshot to ${screenshotPath.toAbsolutePath()}")
                    }
                }
            }

        } else {
            result.isFailure = true
            result.isError = true
            result.failureMessage = "Response is not a Playwright response."
            log.warn("Response is not a playwright sample result.")
        }

        return result
    }
    companion object {
        private const val SELECTOR = "Playwright.selector"
        private const val SELECTOR_TYPE = "Playwright.selectorType"
        private const val ASSERTION_TYPE = "Playwright.assertionType"
        private const val SCREENSHOT_FAILURES = "Playwright.screenshotFailures"
    }
}