package com.microsoft.playwright.jmeter.threadgroup

import com.microsoft.playwright.jmeter.junit.JUnitDeserializer
import org.apache.jmeter.engine.StandardJMeterEngine
import org.apache.jmeter.threads.AbstractThreadGroup
import org.apache.jmeter.threads.JMeterThread
import org.apache.jmeter.threads.ListenerNotifier
import org.apache.jmeter.threads.ThreadGroup
import org.apache.jorphan.collections.ListedHashTree
import org.slf4j.LoggerFactory
import java.io.IOException
import java.nio.file.Path
import kotlin.io.path.Path

class PlaywrightTestThreadGroup : AbstractThreadGroup() {
    private var proc: Process? = null
    private val log = LoggerFactory.getLogger(ThreadGroup::class.java)

    var browserType: BrowserType
        get() = BrowserType.valueOf(getPropertyAsString(BROWSER, "Chromium"))
        set(value) {
            setProperty(BROWSER, value.toString())
        }

    override fun start(groupCount: Int, notifier: ListenerNotifier?, threadGroupTree: ListedHashTree?, engine: StandardJMeterEngine?) {
        log.info("Starting playwright thread pool with ${browserType}.")
        runNpxPlaywrightTest(Path("/users/anthonyshaw/repos"))
    }

    override fun addNewThread(delay: Int, engine: StandardJMeterEngine?): JMeterThread {
        log.info("Adding playwright thread pool with ${browserType}.")
        TODO("implement!")
    }

    override fun verifyThreadsStopped(): Boolean {
        return proc?.isAlive?.not() ?: true
    }

    override fun waitThreadsStopped() {
        while (proc?.isAlive == true) {
            // Sleep?
        }
        val result = proc?.inputStream?.bufferedReader()?.readText() ?: ""
        val errorResult = proc?.errorStream?.bufferedReader()?.readText() ?: ""
        if (errorResult != "")
            log.error(errorResult)
        if (result.trim() == "")
            return
        val testsuites = JUnitDeserializer().deserialize(result)
        log.info("Ran ${testsuites.tests} tests in ${testsuites.time} seconds in ${testsuites.name}.")
    }

    override fun tellThreadsToStop() {
        TODO("Not yet implemented")
    }

    override fun stop() {
        log.info("Stopping playwright instances.")
        proc?.destroyForcibly()
        if (!verifyThreadsStopped()){
            log.warn("Warning: Threads not stopped.")
        }
    }

    override fun threadFinished(thread: JMeterThread?) {
        // TODO("Not yet implemented")
    }

    override fun stopThread(threadName: String?, now: Boolean): Boolean {
        TODO("Not yet implemented")
    }

    override fun numberOfActiveThreads(): Int {
        TODO("Not yet implemented")
    }

    private fun runNpxPlaywrightTest(workingDir: Path) {
        try {
            val parts = arrayListOf("npx", "playwright", "test", "--reporter", "junit")
            // Set number of workers
            parts.add("-j")
            parts.add(this.numThreads.toString())
            log.info("Launching process ${parts}.")
            this.proc = Runtime.getRuntime().exec(parts.joinToString(" ") , null, workingDir.toFile())
        } catch(e: IOException) {
            e.printStackTrace()
        }
    }

    companion object {
        private const val BROWSER = "Playwright.browser"
    }
}