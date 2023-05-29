package com.microsoft.playwright.jmeter.compiler

import com.microsoft.playwright.BrowserContext

interface RunnableJMeterSampler {
    fun run(context: BrowserContext?)
}
