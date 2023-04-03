package com.microsoft.playwright.jmeter

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class PlaywrightBrowserThreadGroupTest {
    @Test
    fun `Create Thread Group`() {
        val threadGroup = PlaywrightBrowserThreadGroup()
        threadGroup.browserType = BrowserType.Webkit
        assertEquals(threadGroup.browserType, BrowserType.Webkit)
    }
}