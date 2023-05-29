package com.microsoft.playwright.jmeter

import com.microsoft.playwright.jmeter.samplers.PlaywrightSampler
import com.microsoft.playwright.jmeter.samplers.PlaywrightSamplerGUI
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test


internal class PlaywrightSamplerGUITest {
    @Test
    fun `Get label`() {
        val panel = PlaywrightSamplerGUI()
        assertEquals(panel.staticLabel, "Playwright Sampler")
    }

    @Test
    fun `configure a test element`(){
        val panel = PlaywrightSamplerGUI()
        val element = panel.createTestElement() as PlaywrightSampler
        panel.configure(element)
        assertEquals(element.url,"https://playwright.dev")
    }
}