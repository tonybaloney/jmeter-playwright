package com.microsoft.playwright.jmeter

import org.apache.jmeter.samplers.Entry

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class PlaywrightSamplerTest {

    @BeforeEach
    fun setUp() {
    }

    @AfterEach
    fun tearDown() {
    }

    @Test
    fun `test setting and getting url properties`() {
        val testUrl = "https://test.com/foo"
        val sampler = PlaywrightSampler()
        sampler.url = testUrl
        assertEquals(sampler.url, testUrl)
    }

    @Test
    fun `test sample microsoft playwright homepage`() {
        val sampler = PlaywrightSampler()
        sampler.url = "https://playwright.dev"
        val result = sampler.sample(null)
        assertEquals(result.url.toString(), sampler.url)
        assertFalse(result.isSuccessful)
    }
}