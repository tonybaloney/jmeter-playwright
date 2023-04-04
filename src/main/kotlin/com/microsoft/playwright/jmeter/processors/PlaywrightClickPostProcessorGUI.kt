package com.microsoft.playwright.jmeter.processors

import org.apache.jmeter.processor.gui.AbstractPostProcessorGui
import org.apache.jmeter.testelement.TestElement
import java.awt.BorderLayout
import javax.swing.Box
import javax.swing.JComboBox
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextField

class PlaywrightClickPostProcessorGUI: AbstractPostProcessorGui() {
    private val selectorComboBox: JComboBox<SelectorType> = JComboBox<SelectorType>(SelectorType.values())
    private val selectorValueInputBox: JTextField = JTextField()
    override fun getLabelResource(): String {
        return "Playwright Click Processor"
    }

    override fun getStaticLabel(): String {
        return labelResource
    }


    init {
        init()
    }

    private fun init() {
        layout = BorderLayout()
        border = makeBorder()
        val box = Box.createVerticalBox()
        box.add(makeTitlePanel())

        // Selector
        val selectorPanel = JPanel(BorderLayout(5, 0))
        val selectorLabel = JLabel("Selector")
        selectorPanel.add(selectorLabel, BorderLayout.WEST)
        selectorComboBox.name = "Playwright.selector"
        selectorLabel.labelFor = selectorComboBox
        selectorPanel.add(selectorComboBox, BorderLayout.CENTER)

        // Value
        val selectorValuePanel = JPanel(BorderLayout(5, 0))
        val selectorValueLabel = JLabel("Input")
        selectorValuePanel.add(selectorValueLabel, BorderLayout.WEST)
        selectorValueInputBox.name = "Playwright.Browser"
        selectorValueLabel.labelFor = selectorValueInputBox
        selectorValuePanel.add(selectorValueInputBox, BorderLayout.CENTER)

        box.add(selectorPanel)
        box.add(selectorValuePanel)

        add(box, BorderLayout.NORTH)
    }



    override fun createTestElement(): TestElement {
        val resultAction = PlaywrightClickPostProcessor()
        modifyTestElement(resultAction)
        return resultAction
    }

    override fun modifyTestElement(element: TestElement) {
        super.configureTestElement(element)
        if (element is PlaywrightClickPostProcessor){
            element.selectorInput = selectorValueInputBox.text
            element.selectorType = selectorComboBox.selectedItem as SelectorType
        }
    }

    override fun configure(element: TestElement) {
        super.configure(element)
        if (element is PlaywrightClickPostProcessor) {
            selectorValueInputBox.text = element.selectorInput
            selectorComboBox.selectedItem = element.selectorType
        }
    }

    override fun clearGui() {
        super.clearGui()
    }
}