package com.microsoft.playwright.jmeter

import com.microsoft.playwright.Browser
import com.microsoft.playwright.Playwright
import org.apache.jmeter.engine.StandardJMeterEngine
import org.apache.jmeter.threads.ListenerNotifier
import org.apache.jmeter.threads.ThreadGroup
import org.apache.jorphan.collections.ListedHashTree
import org.slf4j.LoggerFactory

enum class BrowserType {
    Chromium,
    Firefox,
    Webkit
}

class PlaywrightBrowserThreadGroup : ThreadGroup() {
    private val log = LoggerFactory.getLogger(ThreadGroup::class.java)

    var browserType: BrowserType
        get() = BrowserType.valueOf(getPropertyAsString(BROWSER, "Chromium"))
        set(value) {
            setProperty(BROWSER, value.toString())
        }

    private lateinit var playwright: Playwright
    lateinit var browser: Browser

    override fun start(groupCount: Int, notifier: ListenerNotifier?, threadGroupTree: ListedHashTree?, engine: StandardJMeterEngine?) {
        log.info("Starting playwright thread pool with ${browserType}.")
        playwright = Playwright.create()
        browser = when (browserType) {
            BrowserType.Chromium -> {
                playwright.chromium().launch()
            }
            BrowserType.Webkit -> {
                playwright.webkit().launch()
            }
            BrowserType.Firefox -> {
                playwright.firefox().launch()
            }
        }
        super.start(groupCount, notifier, threadGroupTree, engine)
    }

    override fun tellThreadsToStop() {
        log.info("Stopping browsers.")
        browser.close()
        super.tellThreadsToStop()
    }

    override fun stop() {
        log.info("Stopping playwright instances.")
        playwright.close()
        super.stop()
    }

    companion object {
        private const val BROWSER = "Playwright.browser"
    }
}