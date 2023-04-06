package com.microsoft.playwright.jmeter.processors

import com.microsoft.playwright.jmeter.SelectorType
import org.apache.jmeter.processor.gui.AbstractPostProcessorGui
import org.apache.jmeter.testelement.TestElement
import java.awt.BorderLayout
import javax.swing.*

abstract class AbstractSelectableProcessorGui: AbstractPostProcessorGui() {
    internal val selectorComboBox: JComboBox<SelectorType> = JComboBox<SelectorType>(SelectorType.values())
    internal val selectorValueInputBox: JTextField = JTextField()

    override fun getStaticLabel(): String {
        return labelResource
    }

    internal fun addSelectorPanels(box: Box) {
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
    }

    override fun modifyTestElement(element: TestElement) {
        super.configureTestElement(element)
        if (element is AbstractSelectableProcessor){
            element.selectorInput = selectorValueInputBox.text
            element.selectorType = selectorComboBox.selectedItem as SelectorType
        }
    }

    override fun configure(element: TestElement) {
        super.configure(element)
        if (element is AbstractSelectableProcessor) {
            selectorValueInputBox.text = element.selectorInput
            selectorComboBox.selectedItem = element.selectorType
        }
    }
}