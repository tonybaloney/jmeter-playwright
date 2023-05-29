package com.microsoft.playwright.jmeter.samplers

import com.microsoft.playwright.options.WaitUntilState
import org.apache.jmeter.gui.util.VerticalPanel
import org.apache.jmeter.samplers.gui.AbstractSamplerGui
import org.apache.jmeter.testelement.TestElement
import java.awt.BorderLayout
import javax.swing.*

class PlaywrightSamplerGUI : AbstractSamplerGui() {
    private val urlTextField = JTextField("https://playwright.dev")
    private val refererTextField = JTextField()
    private val timeoutField = JSpinner()
    private val waitUntilComboBox: JComboBox<WaitUntilState> = JComboBox<WaitUntilState>(WaitUntilState.values())
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
                "Playwright Request") // $NON-NLS-1$

        // Test URL
        val urlPanel = JPanel(BorderLayout(5, 0))
        val urlLabel = JLabel("URL")
        urlPanel.add(urlLabel, BorderLayout.WEST)
        urlTextField.name = "Playwright.URL"
        urlLabel.labelFor = urlTextField
        urlPanel.add(urlTextField, BorderLayout.CENTER)

        // wait until..
        val waitUntilPanel = JPanel(BorderLayout(5, 0))
        val waitUntilLabel = JLabel("Wait Until")
        waitUntilPanel.add(waitUntilLabel, BorderLayout.WEST)
        waitUntilComboBox.name = "Playwright.WaitUntil"
        waitUntilLabel.labelFor = waitUntilComboBox
        waitUntilPanel.add(waitUntilComboBox, BorderLayout.CENTER)

        // timeout
        val timeOutPanel = JPanel(BorderLayout(5, 0))
        val timeoutLabel = JLabel("Timeout")
        timeOutPanel.add(timeoutLabel, BorderLayout.WEST)
        timeoutField.name = "Playwright.Timeout"
        timeoutLabel.labelFor = timeoutField
        timeoutField.value = 5000
        timeOutPanel.add(timeoutField, BorderLayout.CENTER)

        // referer
        val refererPanel = JPanel(BorderLayout(5, 0))
        val refererLabel = JLabel("Referer")
        refererPanel.add(refererLabel, BorderLayout.WEST)
        refererTextField.name = "Playwright.Referer"
        refererLabel.labelFor = refererTextField
        refererPanel.add(refererTextField, BorderLayout.CENTER)

        // capture logs
        val capturePanel = JPanel(BorderLayout(5, 0))
        val captureLabel = JLabel("Capture Console Logs")
        capturePanel.add(captureLabel, BorderLayout.WEST)
        captureConsoleLogs.name = "Playwright.captureConsoleLogs"
        captureLabel.labelFor = captureConsoleLogs
        capturePanel.add(captureConsoleLogs, BorderLayout.CENTER)

        propsPanel.add(urlPanel)
        propsPanel.add(waitUntilPanel)
        propsPanel.add(timeOutPanel)
        propsPanel.add(refererPanel)
        propsPanel.add(capturePanel)
        val integrationPanel = VerticalPanel()
        integrationPanel.add(propsPanel)
        add(integrationPanel, BorderLayout.CENTER)
    }

    override fun getLabelResource(): String {
        return "Playwright Sampler"
    }

    override fun getStaticLabel(): String {
        return labelResource
    }

    override fun createTestElement(): TestElement {
        val sampler = PlaywrightSampler()
        modifyTestElement(sampler)
        return sampler
    }

    override fun modifyTestElement(element: TestElement) {
        super.configureTestElement(element)
        if (element is PlaywrightSampler) {
            element.url = urlTextField.text
            element.timeout = (timeoutField.value as Int)
            element.waitUntilState = waitUntilComboBox.selectedItem as WaitUntilState
            element.referer = refererTextField.text
            element.captureConsoleLogs = captureConsoleLogs.isSelected
        }
    }

    override fun configure(element: TestElement) {
        super.configure(element)
        if (element is PlaywrightSampler) {
            urlTextField.text = element.url
            timeoutField.value = element.timeout
            waitUntilComboBox.selectedItem = element.waitUntilState
            refererTextField.text = element.referer
            captureConsoleLogs.isSelected = element.captureConsoleLogs
        }
    }
}
