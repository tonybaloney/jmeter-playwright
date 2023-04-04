package com.microsoft.playwright.jmeter.processors

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class PlaywrightFillPostProcessorGUITest {
    @Test
    fun `configure a test element`(){
        val panel = PlaywrightFillPostProcessorGUI()
        val element = panel.createTestElement() as PlaywrightFillPostProcessor
        panel.configure(element)
        assertEquals(element.selectorType, SelectorType.Title)
        assertEquals(element.selectorInput, "")
        assertEquals(element.fillInput, "")
    }
}