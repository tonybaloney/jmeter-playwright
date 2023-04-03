package com.microsoft.playwright.jmeter

import com.microsoft.playwright.Browser
import com.microsoft.playwright.Playwright
import org.apache.jmeter.engine.StandardJMeterEngine
import org.apache.jmeter.threads.ListenerNotifier
import org.apache.jmeter.threads.ThreadGroup
import org.apache.jorphan.collections.ListedHashTree
import org.slf4j.LoggerFactory
import java.util.concurrent.ConcurrentHashMap

enum class BrowserType {
    Chromium,
    Firefox,
    Webkit
}

class PlaywrightBrowserThreadGroup : ThreadGroup() {
    private val log = LoggerFactory.getLogger(ThreadGroup::class.java)

    private val playwrightInstances = ConcurrentHashMap<Int, Playwright?>()
    private val browserInstances = ConcurrentHashMap<Int, Browser?>()

    var browserType: BrowserType
        get() = BrowserType.valueOf(getPropertyAsString(BROWSER, "Chromium"))
        set(value) {
            setProperty(BROWSER, value.toString())
        }

    override fun start(groupCount: Int, notifier: ListenerNotifier?, threadGroupTree: ListedHashTree?, engine: StandardJMeterEngine?) {
        log.info("Starting playwright thread pool with ${browserType}.")
        super.start(groupCount, notifier, threadGroupTree, engine)
    }

    override fun stop() {
        log.info("Stopping playwright instances.")
        super.stop()
        if (!verifyThreadsStopped()){
            log.warn("Warning: Threads not stopped.")
        }
        browserInstances.forEach {
            log.info("Stopping Playwright ${browserType} browser in thread ${it.key}.")
            it.value?.close()
        }
        playwrightInstances.forEach {
            log.info("Stopping Playwright instance in thread ${it.key}.")
            it.value?.close()
        }
    }

    fun getBrowser(threadNum: Int): Browser? {
        if (playwrightInstances.containsKey(threadNum) && browserInstances.containsKey(threadNum)){
            return browserInstances[threadNum]
        } else {
            log.info("Spawning Playwright instance in thread ${threadNum}.")
            val playwright = Playwright.create()
            playwrightInstances[threadNum] = playwright

            if (browserInstances.containsKey(threadNum)){
                throw Exception("Invalid state. Cannot have a playwright instance with no browser.")
            }
            log.info("Spawning Playwright ${browserType} browser in thread ${threadNum}.")
            val browser = when (browserType) {
                BrowserType.Chromium -> {
                    playwright!!.chromium().launch()
                }
                BrowserType.Webkit -> {
                    playwright!!.webkit().launch()
                }
                BrowserType.Firefox -> {
                    playwright!!.firefox().launch()
                }
            }
            browserInstances[threadNum] = browser
            return browser
        }
    }

    companion object {
        private const val BROWSER = "Playwright.browser"
    }
}