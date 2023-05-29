package com.microsoft.playwright.jmeter

import com.microsoft.playwright.jmeter.threadgroup.BrowserType
import com.microsoft.playwright.jmeter.threadgroup.PlaywrightBrowserThreadGroup
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class PlaywrightBrowserThreadGroupTest {
    @Test
    fun `Create Thread Group`() {
        val threadGroup = PlaywrightBrowserThreadGroup()
        threadGroup.browserType = BrowserType.Webkit
        assertEquals(threadGroup.browserType, BrowserType.Webkit)
    }
}