package com.microsoft.playwright.jmeter

import com.microsoft.playwright.jmeter.samplers.PlaywrightSampler
import com.microsoft.playwright.jmeter.threadgroup.BrowserType
import com.microsoft.playwright.jmeter.threadgroup.PlaywrightBrowserThreadGroup
import org.apache.jmeter.control.LoopController
import org.apache.jmeter.threads.JMeterThread
import org.apache.jorphan.collections.HashTree
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class PlaywrightSamplerTest {
    @Test
    fun `test setting and getting url properties`() {
        val testUrl = "https://test.com/foo"
        val sampler = PlaywrightSampler()
        sampler.url = testUrl
        assertEquals(sampler.url, testUrl)
    }

    @Test
    fun `test sample microsoft playwright homepage`() {
        class DummyThread : JMeterThread(HashTree(LoopController()), null, null) {
            override fun getThreadNum(): Int {
                return 1234
            }
        }

        val threadGroup = PlaywrightBrowserThreadGroup()
        threadGroup.browserType = BrowserType.Webkit

        val sampler = PlaywrightSampler()
        sampler.threadContext.thread = DummyThread()
        sampler.threadContext.threadGroup = threadGroup
        sampler.url = "https://playwright.dev"
        val result = sampler.sample(null)
        assertEquals(result.url.toString(), sampler.url)
        assertTrue(result.isSuccessful)
    }
}