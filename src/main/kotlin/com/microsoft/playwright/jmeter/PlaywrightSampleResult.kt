package com.microsoft.playwright.jmeter
import com.microsoft.playwright.Page
import org.apache.jmeter.samplers.SampleResult

class PlaywrightSampleResult : SampleResult() {
    var page: Page? = null
    var title: String = ""
}