package com.microsoft.playwright.jmeter

import com.microsoft.playwright.Locator
import com.microsoft.playwright.Page

enum class SelectorType {
    Title,
    Generic,
    Text,
    Placeholder,
    TestId,
    Label,
    AltText
}

fun selectAsLocator(selector: SelectorType, page: Page, input: String): Locator {
    return when (selector) {
        SelectorType.Title -> {
            page.getByTitle(input)
        }

        SelectorType.Generic -> {
            page.locator(input)
        }

        SelectorType.Text -> {
            page.getByText(input)
        }

        SelectorType.Placeholder -> {
            page.getByPlaceholder(input)
        }

        SelectorType.TestId -> {
            page.getByTestId(input)
        }

        SelectorType.Label -> {
            page.getByLabel(input)
        }

        SelectorType.AltText -> {
            page.getByAltText(input)
        }
    }
}