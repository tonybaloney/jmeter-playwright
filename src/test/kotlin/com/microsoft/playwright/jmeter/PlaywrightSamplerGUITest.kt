package com.microsoft.playwright.jmeter

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*


internal class PlaywrightSamplerGUITest {
    @Test
    fun `Get label`() {
        val panel = PlaywrightSamplerGUI()
        assertEquals(panel.staticLabel, "Playwright Sampler");
    }

    @Test
    fun `configure a test element`(){
        val panel = PlaywrightSamplerGUI()
        val element = panel.createTestElement() as PlaywrightSampler
        panel.configure(element)
        assertEquals(element.url,"https://playwright.dev")
    }
}