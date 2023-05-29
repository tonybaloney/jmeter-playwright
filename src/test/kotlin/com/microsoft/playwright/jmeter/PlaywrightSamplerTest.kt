package com.microsoft.playwright.jmeter

import com.microsoft.playwright.jmeter.samplers.PlaywrightSampler
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class PlaywrightSamplerTest {
    @Test
    fun `test setting and getting url properties`() {
        val testUrl = "https://test.com/foo"
        val sampler = PlaywrightSampler()
        sampler.url = testUrl
        assertEquals(sampler.url, testUrl)
    }

//    @Test
//    fun `test sample microsoft playwright homepage`() {
//        class DummyThread : JMeterThread(HashTree(LoopController()), null, null) {
//            override fun getThreadNum(): Int {
//                return 1234
//            }
//        }
//
//        val threadGroup = PlaywrightBrowserThreadGroup()
//        threadGroup.browserType = BrowserType.Webkit
//
//        val sampler = PlaywrightSampler()
//        sampler.threadContext.thread = DummyThread()
//        sampler.threadContext.threadGroup = threadGroup
//        sampler.url = "https://playwright.dev"
//        sampler.timeout = 15000
//        val result = sampler.sample(null)
//        assertEquals(result.url.toString(), sampler.url)
//        assertTrue(result.isSuccessful)
//    }
}