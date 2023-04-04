package com.microsoft.playwright.jmeter.processors

import org.apache.jmeter.testelement.TestElement
import java.awt.BorderLayout
import javax.swing.Box
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextField

class PlaywrightFillPostProcessorGUI: AbstractSelectableProcessorGui() {
    private val fillInputTextField: JTextField = JTextField()

    override fun getLabelResource(): String {
        return "Playwright Fill Processor"
    }

    init {
        init()
    }

    private fun init() {
        layout = BorderLayout()
        border = makeBorder()
        val box = Box.createVerticalBox()
        box.add(makeTitlePanel())

        addSelectorPanels(box)

        val fillInputPanel = JPanel(BorderLayout(5, 0))
        val fillInputLabel = JLabel("Fill Input")
        fillInputPanel.add(fillInputLabel, BorderLayout.WEST)
        fillInputTextField.name = "Playwright.FillInput"
        fillInputLabel.labelFor = fillInputTextField
        fillInputPanel.add(fillInputTextField, BorderLayout.CENTER)
        box.add(fillInputPanel)

        add(box, BorderLayout.NORTH)
    }

    override fun createTestElement(): TestElement {
        val resultAction = PlaywrightFillPostProcessor()
        modifyTestElement(resultAction)
        return resultAction
    }

    override fun modifyTestElement(element: TestElement) {
        super.modifyTestElement(element)
        if (element is PlaywrightFillPostProcessor){
            element.fillInput = fillInputTextField.text
        }
    }

    override fun configure(element: TestElement) {
        super.configure(element)
        if (element is PlaywrightFillPostProcessor) {
            fillInputTextField.text = element.fillInput
        }
    }
}