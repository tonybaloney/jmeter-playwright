package com.microsoft.playwright.jmeter;

import org.apache.jmeter.control.LoopController;
import org.apache.jmeter.control.gui.LoopControlPanel;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.threads.AbstractThreadGroup;
import org.apache.jmeter.threads.gui.AbstractThreadGroupGui;
import org.apache.jmeter.util.JMeterUtils;

import javax.swing.*;
import java.awt.*;

public class PlaywrightBrowserThreadGroupGUI extends AbstractThreadGroupGui {
    String[] browserOptions = {"Chromium", "Firefox", "Webkit"};
    private final JComboBox browserComboBox = new JComboBox(browserOptions);
    private LoopControlPanel loopPanel;

    public PlaywrightBrowserThreadGroupGUI() {
        super();
        init();
        initGui();
    }

    private void init(){
        // THREAD PROPERTIES
        VerticalPanel threadPropsPanel = new VerticalPanel();
        threadPropsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
                JMeterUtils.getResString("thread_properties"))); // $NON-NLS-1$

        // RAMP-UP
        JPanel browserPanel = new JPanel(new BorderLayout(5, 0));
        JLabel browserLabel = new JLabel("Browser");
        browserPanel.add(browserLabel, BorderLayout.WEST);

        browserComboBox.setName("Playwright.Browser");
        browserLabel.setLabelFor(browserComboBox);
        browserPanel.add(browserComboBox, BorderLayout.CENTER);

        threadPropsPanel.add(browserPanel);

        threadPropsPanel.add(createControllerPanel());
        VerticalPanel integrationPanel = new VerticalPanel();
        integrationPanel.add(threadPropsPanel);
        add(integrationPanel, BorderLayout.CENTER);
    }

    @Override
    public String getLabelResource() {
        return "Playwright Thread Group";
    }

    @Override
    public String getStaticLabel() {
        return getLabelResource();
    }

    @Override
    public TestElement createTestElement() {
        PlaywrightBrowserThreadGroup threadGroup = new PlaywrightBrowserThreadGroup();
        modifyTestElement(threadGroup);
        return threadGroup;
    }

    @Override
    public void modifyTestElement(TestElement element) {
        super.configureTestElement(element);
        if (element instanceof AbstractThreadGroup) {
            ((AbstractThreadGroup) element).setSamplerController((LoopController) loopPanel.createTestElement());
        }

        if (element instanceof PlaywrightBrowserThreadGroup) {
            PlaywrightBrowserThreadGroup threadGroup = (PlaywrightBrowserThreadGroup) element;
            int selectedIndex = browserComboBox.getSelectedIndex();
            if (selectedIndex == 0) {
                threadGroup.setBrowserType(BrowserType.Chromium);
            } else if (selectedIndex == 1) {
                threadGroup.setBrowserType(BrowserType.Firefox);
            } else if (selectedIndex == 2) {
                threadGroup.setBrowserType(BrowserType.Webkit);
            }
            threadGroup.setNumThreads(1);
        }
    }

    @Override
    public void configure(TestElement element) {
        super.configure(element);
        loopPanel.configure((TestElement) element.getProperty(AbstractThreadGroup.MAIN_CONTROLLER).getObjectValue());
        if (element instanceof PlaywrightBrowserThreadGroup) {
            PlaywrightBrowserThreadGroup threadGroup = (PlaywrightBrowserThreadGroup) element;
            BrowserType browserType = threadGroup.getBrowserType();
            if (browserType == BrowserType.Chromium) {
                browserComboBox.setSelectedItem(0);
            } else if (browserType == BrowserType.Firefox) {
                browserComboBox.setSelectedItem(1);
            } else if (browserType == BrowserType.Webkit) {
                browserComboBox.setSelectedItem(2);
            }
        }
    }

    private JPanel createControllerPanel() {
        loopPanel = new LoopControlPanel(false);
        LoopController looper = (LoopController) loopPanel.createTestElement();
        looper.setLoops(1);
        loopPanel.configure(looper);
        return loopPanel;
    }

    @Override
    public void clearGui(){
        super.clearGui();
        initGui();
    }

    private void initGui(){
        loopPanel.clearGui();
    }
}
