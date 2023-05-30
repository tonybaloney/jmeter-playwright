package com.microsoft.playwright.jmeter.samplers

import org.apache.jmeter.gui.util.JSyntaxTextArea
import org.apache.jmeter.gui.util.VerticalPanel
import org.apache.jmeter.samplers.gui.AbstractSamplerGui
import org.apache.jmeter.testelement.TestElement
import java.awt.BorderLayout
import javax.swing.*

@Suppress("unused")
class PlaywrightScriptedSamplerGUI : AbstractSamplerGui() {
    private val nameTextField = JTextField("")
    private val codeTextArea = JSyntaxTextArea.getInstance(40, 120)
    private val captureConsoleLogs = JCheckBox()

    init {
        layout = BorderLayout(0, 5)
        border = makeBorder()
        val box = Box.createVerticalBox()
        box.add(makeTitlePanel())
        add(box, BorderLayout.NORTH)
        init()
    }

    private fun init() {
        val propsPanel = VerticalPanel()
        propsPanel.border = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
                "Playwright Script") // $NON-NLS-1$

        // name
        val namePanel = JPanel(BorderLayout(5, 0))
        val nameLabel = JLabel("Name")
        namePanel.add(nameLabel, BorderLayout.WEST)
        nameTextField.name = "Playwright.name"
        nameTextField.text = "testpackage.Example"
        nameLabel.labelFor = nameTextField
        namePanel.add(nameTextField, BorderLayout.CENTER)

        // capture logs
        val capturePanel = JPanel(BorderLayout(5, 0))
        val captureLabel = JLabel("Capture Console Logs")
        capturePanel.add(captureLabel, BorderLayout.WEST)
        captureConsoleLogs.name = "Playwright.captureConsoleLogs"
        captureLabel.labelFor = captureConsoleLogs
        capturePanel.add(captureConsoleLogs, BorderLayout.CENTER)

        // code..
        val codePanel = JPanel(BorderLayout(5, 0))
        val codeLabel = JLabel("Code")
        codePanel.add(codeLabel, BorderLayout.WEST)
        codeTextArea.name = "Playwright.code"
        codeTextArea.text = """
        package testpackage;

        import com.microsoft.playwright.BrowserContext;
        import com.microsoft.playwright.Page;
        import com.microsoft.playwright.jmeter.compiler.RunnableJMeterSampler;
        import com.microsoft.playwright.options.AriaRole;
        
        public class Example implements RunnableJMeterSampler {
            @Override
            public void run(BrowserContext context) {
                // Your code here!
                Page page = context.newPage();
                page.navigate("https://playwright.dev/");
                page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Get started")).click();
            }
        }
        """.trimIndent()
        codeLabel.labelFor = codeTextArea
        codePanel.add(codeTextArea, BorderLayout.CENTER)

        propsPanel.add(namePanel)
        propsPanel.add(codePanel)
        propsPanel.add(capturePanel)

        val integrationPanel = VerticalPanel()
        integrationPanel.add(propsPanel)
        add(integrationPanel, BorderLayout.CENTER)
    }

    override fun getLabelResource(): String {
        return "Playwright Scripted Sampler"
    }

    override fun getStaticLabel(): String {
        return labelResource
    }

    override fun createTestElement(): TestElement {
        val sampler = PlaywrightScriptedSampler()
        modifyTestElement(sampler)
        return sampler
    }

    override fun modifyTestElement(element: TestElement) {
        super.configureTestElement(element)
        if (element is PlaywrightScriptedSampler) {
            element.className = nameTextField.text
            element.code = codeTextArea.text
            element.captureConsoleLogs = captureConsoleLogs.isSelected
        }
    }

    override fun configure(element: TestElement) {
        super.configure(element)
        if (element is PlaywrightScriptedSampler) {
            nameTextField.text = element.className
            codeTextArea.text = element.code
            captureConsoleLogs.isSelected = element.captureConsoleLogs
        }
    }
}
