package com.microsoft.playwright.jmeter.processors

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class PlaywrightClickPostProcessorGUITest {
    @Test
    fun `configure a test element`(){
        val panel = PlaywrightClickPostProcessorGUI()
        val element = panel.createTestElement() as PlaywrightClickPostProcessor
        panel.configure(element)
        assertEquals(element.selectorType, SelectorType.Title)
        assertEquals(element.selectorInput, "")
    }
}