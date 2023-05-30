package com.microsoft.playwright.jmeter.assertions

import com.microsoft.playwright.jmeter.SelectorType
import org.apache.jmeter.assertions.gui.AbstractAssertionGui
import org.apache.jmeter.testelement.TestElement
import java.awt.BorderLayout
import javax.swing.*

class PlaywrightAssertionGUI : AbstractAssertionGui() {
    private val selectorComboBox: JComboBox<SelectorType> = JComboBox<SelectorType>(SelectorType.values())
    private val selectorValueInputBox: JTextField = JTextField()
    private val assertionTypeComboBox: JComboBox<AssertionType> = JComboBox<AssertionType>(AssertionType.values())
    private val takeScreenshotOptionBox: JCheckBox = JCheckBox()
    override fun getLabelResource(): String {
        return "Playwright Assertion"
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
        selectorValueInputBox.name = "Playwright.selectorValue"
        selectorValueLabel.labelFor = selectorValueInputBox
        selectorValuePanel.add(selectorValueInputBox, BorderLayout.CENTER)

        // assertion
        val assertionPanel = JPanel(BorderLayout(5, 0))
        val assertionLabel = JLabel("Assertion")
        assertionPanel.add(assertionLabel, BorderLayout.WEST)
        assertionTypeComboBox.name = "Playwright.assertion"
        assertionLabel.labelFor = assertionTypeComboBox
        assertionPanel.add(assertionTypeComboBox, BorderLayout.CENTER)

        // assertion
        val screenshotPanel = JPanel(BorderLayout(5, 0))
        val screenshotLabel = JLabel("Screenshot on Failure")
        screenshotPanel.add(screenshotLabel, BorderLayout.WEST)
        takeScreenshotOptionBox.name = "Playwright.screenshotFailure"
        screenshotLabel.labelFor = takeScreenshotOptionBox
        screenshotPanel.add(takeScreenshotOptionBox, BorderLayout.CENTER)

        box.add(selectorPanel)
        box.add(selectorValuePanel)
        box.add(assertionPanel)

        add(box, BorderLayout.NORTH)
    }

    override fun createTestElement(): TestElement {
        val resultAction = PlaywrightAssertion()
        modifyTestElement(resultAction)
        return resultAction
    }

    override fun modifyTestElement(element: TestElement) {
        super.configureTestElement(element)
        if (element is PlaywrightAssertion){
            element.selectorInput = selectorValueInputBox.text
            element.selectorType = selectorComboBox.selectedItem as SelectorType
            element.assertionType = assertionTypeComboBox.selectedItem as AssertionType
            element.takeScreenshotOnFailure = takeScreenshotOptionBox.isSelected
        }
    }

    override fun configure(element: TestElement) {
        super.configure(element)
        if (element is PlaywrightAssertion) {
            selectorValueInputBox.text = element.selectorInput
            selectorComboBox.selectedItem = element.selectorType
            assertionTypeComboBox.selectedItem = element.assertionType
            takeScreenshotOptionBox.isSelected = element.takeScreenshotOnFailure
        }
    }
}