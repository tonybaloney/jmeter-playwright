package com.microsoft.playwright.jmeter.processors

import com.microsoft.playwright.Locator
import com.microsoft.playwright.Page
import org.apache.jmeter.processor.PostProcessor
import org.apache.jmeter.testelement.AbstractTestElement

enum class SelectorType {
    Title,
    XPath,
    Text;
}

fun select(selector: SelectorType, page: Page, input: String): Locator {
    return when (selector) {
        SelectorType.Title -> {
            page.getByTitle(input)
        }

        SelectorType.XPath -> {
            page.locator(input)
        }

        SelectorType.Text -> {
            page.getByText(input)
        }
    }
}

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