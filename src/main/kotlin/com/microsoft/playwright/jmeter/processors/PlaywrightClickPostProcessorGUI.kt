package com.microsoft.playwright.jmeter.processors

import org.apache.jmeter.testelement.TestElement
import java.awt.BorderLayout
import javax.swing.Box

class PlaywrightClickPostProcessorGUI: AbstractSelectableProcessorGui() {

    override fun getLabelResource(): String {
        return "Playwright Click Processor"
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

        add(box, BorderLayout.NORTH)
    }

    override fun createTestElement(): TestElement {
        val resultAction = PlaywrightClickPostProcessor()
        modifyTestElement(resultAction)
        return resultAction
    }
}