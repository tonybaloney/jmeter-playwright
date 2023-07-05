package com.microsoft.playwright.jmeter.samplers

import com.microsoft.playwright.jmeter.BrowserType
import com.microsoft.playwright.jmeter.ui.PlaywrightFileFilter
import org.apache.jmeter.gui.util.VerticalPanel
import org.apache.jmeter.samplers.gui.AbstractSamplerGui
import org.apache.jmeter.testelement.TestElement
import java.awt.BorderLayout
import javax.swing.*

class PlaywrightTestSamplerGUI : AbstractSamplerGui() {
    private var testDirectory: JFileChooser = JFileChooser()
    private var configFile: JFileChooser = JFileChooser()
    private var extraOptions: JTextField = JTextField()
    private var workerCount: JSpinner = JSpinner()
    private var timeout: JSpinner = JSpinner()
    private var repeatEach: JSpinner = JSpinner()
    private var grep: JTextField = JTextField()
    private var grepInvert: JTextField = JTextField()
    private var project: JTextField = JTextField()

    init {
        layout = BorderLayout(0, 5)
        border = makeBorder()
        val box = Box.createVerticalBox()
        box.add(makeTitlePanel())
        add(box, BorderLayout.NORTH)
        init()
    }

    private fun init() {
        // Test directory
        val testDirectoryPanel = JPanel(BorderLayout(5, 0))
        val testDirectoryLabel = JLabel("Test Directory")
        testDirectoryPanel.add(testDirectoryLabel, BorderLayout.WEST)
        testDirectory.name = "Playwright.TestDirectory"
        testDirectoryLabel.labelFor = testDirectory
        testDirectory.fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
        testDirectory.controlButtonsAreShown = false
        testDirectoryPanel.add(testDirectory, BorderLayout.CENTER)

        // Config path
        val configFilePanel = JPanel(BorderLayout(5, 0))
        val configFileLabel = JLabel("Playwright Config (optional)")
        configFilePanel.add(configFileLabel, BorderLayout.WEST)
        configFile.name = "Playwright.configFile"
        configFileLabel.labelFor = configFile
        configFile.fileSelectionMode = JFileChooser.FILES_ONLY
        configFile.fileFilter = PlaywrightFileFilter()
        configFile.controlButtonsAreShown = false
        configFilePanel.add(configFile, BorderLayout.CENTER)

        // Test directory
        val extraOptionsPanel = JPanel(BorderLayout(5, 0))
        val extraOptionsLabel = JLabel("Extra CLI Options (optional)")
        extraOptionsPanel.add(extraOptionsLabel, BorderLayout.WEST)
        extraOptions.name = "Playwright.extraOptions"
        extraOptionsLabel.labelFor = extraOptions
        extraOptionsPanel.add(extraOptions, BorderLayout.CENTER)

        // timeut
        val timeoutPanel = JPanel(BorderLayout(5, 0))
        val timeoutLabel = JLabel("Timeout (optional)")
        timeoutPanel.add(timeoutLabel, BorderLayout.WEST)
        timeout.name = "Playwright.timeout"
        timeoutLabel.labelFor = timeout
        timeoutPanel.add(timeout, BorderLayout.CENTER)

        // worker count
        val workerCountPanel = JPanel(BorderLayout(5, 0))
        val workerCountLabel = JLabel("Worker Count (optional)")
        workerCountPanel.add(workerCountLabel, BorderLayout.WEST)
        workerCount.name = "Playwright.workerCount"
        workerCountLabel.labelFor = workerCount
        workerCountPanel.add(workerCount, BorderLayout.CENTER)

        // repeat each
        val repeatEachPanel = JPanel(BorderLayout(5, 0))
        val repeatEachLabel = JLabel("Repeat Each Test (optional)")
        repeatEachPanel.add(repeatEachLabel, BorderLayout.WEST)
        repeatEach.name = "Playwright.repeatEach"
        repeatEachLabel.labelFor = repeatEach
        repeatEachPanel.add(repeatEach, BorderLayout.CENTER)

        // grep
        val grepPanel = JPanel(BorderLayout(5, 0))
        val grepLabel = JLabel("Test Filter (optional)")
        grepPanel.add(grepLabel, BorderLayout.WEST)
        grep.name = "Playwright.grep"
        grepLabel.labelFor = grep
        grepPanel.add(grep, BorderLayout.CENTER)

        // grep invert
        val grepInvertPanel = JPanel(BorderLayout(5, 0))
        val grepInvertLabel = JLabel("Inverted Test Filter (optional)")
        grepInvertPanel.add(grepInvertLabel, BorderLayout.WEST)
        grepInvert.name = "Playwright.grepInvert"
        grepInvertLabel.labelFor = grepInvert
        grepInvertPanel.add(grepInvert, BorderLayout.CENTER)

        // project name
        val projectPanel = JPanel(BorderLayout(5, 0))
        val projectLabel = JLabel("Project Name (optional)")
        projectPanel.add(projectLabel, BorderLayout.WEST)
        project.name = "Playwright.project"
        projectLabel.labelFor = project
        projectPanel.add(project, BorderLayout.CENTER)

        val integrationPanel = VerticalPanel()
        integrationPanel.add(testDirectoryPanel)
        integrationPanel.add(configFilePanel)
        integrationPanel.add(workerCountPanel)
        integrationPanel.add(repeatEachPanel)
        integrationPanel.add(grepPanel)
        integrationPanel.add(grepInvertPanel)
        integrationPanel.add(projectPanel)
        integrationPanel.add(extraOptionsPanel)

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
            element.testDirectory = testDirectory.selectedFile
            element.configFile = configFile.selectedFile
            element.workerCount = workerCount.value as Int
            element.timeout = timeout.value as Int
            element.repeatEach = repeatEach.value as Int
            element.extraOptions = extraOptions.text
            element.grep = grep.text
            element.grepInvert = grepInvert.text
            element.project = project.text
        }
    }

    override fun configure(element: TestElement) {
        super.configure(element)
        if (element is PlaywrightTestSampler) {
            testDirectory.selectedFile = element.testDirectory
            configFile.selectedFile = element.configFile
            timeout.value = element.timeout
            repeatEach.value = element.repeatEach
            workerCount.value = element.workerCount
            extraOptions.text = element.extraOptions
            grep.text = element.grep
            grepInvert.text = element.grepInvert
            project.text = element.project
        }
    }
}
