package com.microsoft.playwright.jmeter

import com.microsoft.playwright.jmeter.threadgroup.BrowserType
import com.microsoft.playwright.jmeter.threadgroup.PlaywrightBrowserThreadGroup
import com.microsoft.playwright.jmeter.threadgroup.PlaywrightBrowserThreadGroupGUI
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class PlaywrightBrowserThreadGroupGUITest  {
    @Test
    fun `configure a test element`(){
        val panel = PlaywrightBrowserThreadGroupGUI()
        val element = panel.createTestElement() as PlaywrightBrowserThreadGroup
        panel.configure(element)
        Assertions.assertEquals(element.browserType, BrowserType.Chromium)
    }
}