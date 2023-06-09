package com.microsoft.playwright.jmeter.samplers

import com.microsoft.playwright.jmeter.BrowserType
import org.apache.jmeter.gui.util.VerticalPanel
import org.apache.jmeter.samplers.gui.AbstractSamplerGui
import org.apache.jmeter.testelement.TestElement
import java.awt.BorderLayout
import javax.swing.*

class PlaywrightTestSamplerGUI : AbstractSamplerGui() {
    private val browserComboBox: JComboBox<BrowserType> = JComboBox<BrowserType>(BrowserType.values())
    private var testDirectory: JFileChooser = JFileChooser()
    private var extraOptions: JTextField = JTextField()

    init {
        layout = BorderLayout(0, 5)
        border = makeBorder()
        val box = Box.createVerticalBox()
        box.add(makeTitlePanel())
        add(box, BorderLayout.NORTH)
        init()
    }

    private fun init() {
        // Browser
        val browserPanel = JPanel(BorderLayout(5, 0))
        val browserLabel = JLabel("Browser")
        browserPanel.add(browserLabel, BorderLayout.WEST)
        browserComboBox.name = "Playwright.Browser"
        browserLabel.labelFor = browserComboBox
        browserPanel.add(browserComboBox, BorderLayout.CENTER)

        // Test directory
        val testDirectoryPanel = JPanel(BorderLayout(5, 0))
        val testDirectoryLabel = JLabel("Test Directory")
        testDirectoryPanel.add(testDirectoryLabel, BorderLayout.WEST)
        testDirectory.name = "Playwright.TestDirectory"
        testDirectoryLabel.labelFor = testDirectory
        testDirectory.fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
        testDirectoryPanel.add(testDirectory, BorderLayout.CENTER)

        // Test directory
        val extraOptionsPanel = JPanel(BorderLayout(5, 0))
        val extraOptionsLabel = JLabel("Extra CLI Options")
        extraOptionsPanel.add(extraOptionsLabel, BorderLayout.WEST)
        extraOptions.name = "Playwright.extraOptions"
        extraOptionsLabel.labelFor = extraOptions
        extraOptionsPanel.add(extraOptions, BorderLayout.CENTER)


        val integrationPanel = VerticalPanel()
        integrationPanel.add(browserPanel)
        integrationPanel.add(testDirectoryPanel)
        integrationPanel.add(extraOptions)
        add(integrationPanel, BorderLayout.CENTER)
    }

    override fun getLabelResource(): String {
        return "Playwright Test Sampler"
    }

    override fun getStaticLabel(): String {
        return labelResource
    }

    override fun createTestElement(): TestElement {
        val threadGroup = PlaywrightTestSampler()
        modifyTestElement(threadGroup)
        return threadGroup
    }

    override fun modifyTestElement(element: TestElement) {
        super.configureTestElement(element)
        if (element is PlaywrightTestSampler) {
            element.browserType = browserComboBox.selectedItem as BrowserType
            element.testDirectory = testDirectory.selectedFile ?: testDirectory.currentDirectory
            element.extraOptions = extraOptions.text
        }
    }

    override fun configure(element: TestElement) {
        super.configure(element)
        if (element is PlaywrightTestSampler) {
            browserComboBox.selectedItem = element.browserType
            testDirectory.selectedFile = element.testDirectory
            extraOptions.text = element.extraOptions
        }
    }
}
