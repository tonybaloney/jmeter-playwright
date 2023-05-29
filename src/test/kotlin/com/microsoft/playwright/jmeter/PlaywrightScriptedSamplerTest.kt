package com.microsoft.playwright.jmeter

import com.microsoft.playwright.BrowserContext
import com.microsoft.playwright.Page
import com.microsoft.playwright.jmeter.compiler.JavaStringCompiler
import org.junit.jupiter.api.Test
import org.mockito.Mockito.times
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class PlaywrightScriptedSamplerTest {
    @Test
    fun `test basic compilation`() {
        val code = """
        package testpackage;

        import com.microsoft.playwright.BrowserContext;
        import com.microsoft.playwright.Page;
        import com.microsoft.playwright.jmeter.compiler.RunnableJMeterSampler;
        import com.microsoft.playwright.options.AriaRole;

        public class Example implements RunnableJMeterSampler {
            @Override
            public void run(BrowserContext context) {
                // Your code here!
                context.newPage();
            }
        }
        """.trimIndent()
        val name = "testpackage.Example"
        val cc = JavaStringCompiler()
        val cls = cc.compile(name, code)

        val dummyPage = mock<Page> {

        }

        val contextMock = mock<BrowserContext> {
            on { newPage() } doAnswer { dummyPage }
        }
        cls.run(contextMock)

        verify(contextMock, times(1)).newPage()
    }
}

