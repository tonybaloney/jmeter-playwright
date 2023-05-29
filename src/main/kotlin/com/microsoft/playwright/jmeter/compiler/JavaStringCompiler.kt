package com.microsoft.playwright.jmeter.compiler

import net.openhft.compiler.CachedCompiler
import org.apache.jmeter.threads.ThreadGroup
import org.slf4j.LoggerFactory
import java.io.PrintWriter
import java.io.StringWriter
import java.util.concurrent.locks.ReentrantLock

private val log = LoggerFactory.getLogger(ThreadGroup::class.java)
private val compilerLock = ReentrantLock()
class JavaStringCompiler {
    private val CACHED_COMPILER = CachedCompiler(null, null, listOf("-g", "--add-opens", "java.base/java.lang=ALL-UNNAMED"))

    fun compile(className: String, code: String): RunnableJMeterSampler {
        val out = StringWriter()
        val writer = PrintWriter(out)
        try {
            var aClass: Class<*>
            // TODO: Store a hashmap of the compiled classes to detect if it exists in the classloader but the data has changed.
            synchronized(compilerLock) {
                try {
                    aClass = javaClass.classLoader.loadClass(className)
                } catch (e: ClassNotFoundException) {
                    aClass = CACHED_COMPILER.loadFromJava(javaClass.classLoader, className, code, writer)
                    log.info("Compiled ${aClass.classes}")
                }
            }
            writer.flush()
            val logs = out.toString()
            if (logs.trim() != "") {
                log.info(logs)
            }
            return aClass.getDeclaredConstructor().newInstance() as RunnableJMeterSampler

        } catch(r: Exception) {
            writer.flush()
            log.error(out.toString())
            throw r
        }
    }
}