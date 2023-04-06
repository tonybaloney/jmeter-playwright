package com.microsoft.playwright.jmeter.processors

import com.microsoft.playwright.jmeter.SelectorType
import org.apache.jmeter.processor.PostProcessor
import org.apache.jmeter.testelement.AbstractTestElement

abstract class AbstractSelectableProcessor: PostProcessor, AbstractTestElement() {
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

    companion object {
        private const val SELECTOR = "Playwright.selector"
        private const val SELECTOR_TYPE = "Playwright.selectorType"
    }
}