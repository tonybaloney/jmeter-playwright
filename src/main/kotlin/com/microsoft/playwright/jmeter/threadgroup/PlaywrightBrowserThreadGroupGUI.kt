package com.microsoft.playwright.jmeter.threadgroup

import org.apache.jmeter.control.LoopController
import org.apache.jmeter.control.gui.LoopControlPanel
import org.apache.jmeter.gui.util.VerticalPanel
import org.apache.jmeter.testelement.TestElement
import org.apache.jmeter.threads.AbstractThreadGroup
import org.apache.jmeter.threads.gui.AbstractThreadGroupGui
import org.apache.jmeter.util.JMeterUtils
import java.awt.BorderLayout
import javax.swing.*

class PlaywrightBrowserThreadGroupGUI : AbstractThreadGroupGui() {
    private val browserComboBox: JComboBox<BrowserType> = JComboBox<BrowserType>(BrowserType.values())
    private var loopPanel: LoopControlPanel = LoopControlPanel(false)
    private var threadInput: JTextField = JTextField(5)

    init {
        init()
        initGui()
    }

    private fun init() {
        // THREAD PROPERTIES
        val threadPropsPanel = VerticalPanel()
        threadPropsPanel.border = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
                JMeterUtils.getResString("thread_properties")) // $NON-NLS-1$

        // NUMBER OF THREADS
        val threadPanel = JPanel(BorderLayout(5, 0))
        val threadLabel = JLabel(JMeterUtils.getResString("number_of_threads")) // $NON-NLS-1$
        threadPanel.add(threadLabel, BorderLayout.WEST)

        threadInput.name = THREAD_NAME
        threadLabel.labelFor = threadInput
        threadPanel.add(threadInput, BorderLayout.CENTER)
        threadPropsPanel.add(threadPanel)

        // Browser
        val browserPanel = JPanel(BorderLayout(5, 0))
        val browserLabel = JLabel("Browser")
        browserPanel.add(browserLabel, BorderLayout.WEST)
        browserComboBox.name = "Playwright.Browser"
        browserLabel.labelFor = browserComboBox
        browserPanel.add(browserComboBox, BorderLayout.CENTER)
        threadPropsPanel.add(browserPanel)
        createControllerPanel()
        threadPropsPanel.add(loopPanel)
        val integrationPanel = VerticalPanel()
        integrationPanel.add(threadPropsPanel)
        add(integrationPanel, BorderLayout.CENTER)
    }

    override fun getLabelResource(): String {
        return "Playwright Thread Group"
    }

    override fun getStaticLabel(): String {
        return labelResource
    }

    override fun createTestElement(): TestElement {
        val threadGroup = PlaywrightBrowserThreadGroup()
        modifyTestElement(threadGroup)
        return threadGroup
    }

    override fun modifyTestElement(element: TestElement) {
        super.configureTestElement(element)
        if (element is AbstractThreadGroup) {
            element.setSamplerController(loopPanel.createTestElement() as LoopController)
        }
        element.setProperty(AbstractThreadGroup.NUM_THREADS, threadInput.text)
        if (element is PlaywrightBrowserThreadGroup) {
            element.browserType = browserComboBox.selectedItem as BrowserType
        }
    }

    override fun configure(element: TestElement) {
        super.configure(element)
        threadInput.text = element.getPropertyAsString(AbstractThreadGroup.NUM_THREADS)
        loopPanel.configure(element.getProperty(AbstractThreadGroup.MAIN_CONTROLLER).objectValue as TestElement)
        if (element is PlaywrightBrowserThreadGroup) {
            browserComboBox.selectedItem = element.browserType
        }
    }

    private fun createControllerPanel(){
        val looper = loopPanel.createTestElement() as LoopController
        looper.loops = 1
        loopPanel.configure(looper)
    }

    override fun clearGui() {
        super.clearGui()
        initGui()
    }

    private fun initGui() {
        loopPanel.clearGui()
        threadInput.text = "1" // $NON-NLS-1$
    }

    companion object {
        private const val THREAD_NAME = "Thread Field"
    }
}
