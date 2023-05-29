package com.microsoft.playwright.jmeter

import com.microsoft.playwright.BrowserContext
import com.microsoft.playwright.jmeter.compiler.RunnableJMeterSampler

class PlaywrightScriptedSamplerTest {
    class SamplerImpl : RunnableJMeterSampler {
        override fun run(context: BrowserContext?) {
            TODO("Not yet implemented")
        }
    }

//    @Test
//    fun `test basic compilation`() {
//        val code = """
//        package testpackage;
//
//        import com.microsoft.playwright.BrowserContext;
//        import com.microsoft.playwright.Page;
//        import com.microsoft.playwright.jmeter.compiler.RunnableJMeterSampler;
//        import com.microsoft.playwright.options.AriaRole;
//
//        public class Example implements RunnableJMeterSampler {
//            @Override
//            public void run(BrowserContext context) {
//                // Your code here!
//                Page page = context.newPage();
//                page.navigate("https://playwright.dev/");
//                page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Get started")).click();
//            }
//        }
//        """.trimIndent()
//        val name = "testpackage.Example"
//        val cc = JavaStringCompiler(name, code)
//        val cls = cc.compile()
//        cls.run(null)
//    }
}