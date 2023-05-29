package com.microsoft.playwright.jmeter.assertions

import com.microsoft.playwright.jmeter.SelectorType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class PlaywrightAssertionGUITest {
    @Test
    fun `configure a test element`(){
        val panel = PlaywrightAssertionGUI()
        val element = panel.createTestElement() as PlaywrightAssertion
        panel.configure(element)
        assertEquals(element.selectorType, SelectorType.Title)
        assertEquals(element.selectorInput, "")
        assertEquals(element.assertionType, AssertionType.IsChecked)
    }
}