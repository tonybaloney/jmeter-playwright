package com.microsoft.playwright.jmeter;

import com.microsoft.playwright.options.WaitUntilState;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.samplers.gui.AbstractSamplerGui;
import org.apache.jmeter.testelement.TestElement;

import javax.swing.*;
import java.awt.*;

import static com.microsoft.playwright.jmeter.PlaywrightSamplerKt.waitUntilStateFromString;
import static com.microsoft.playwright.jmeter.PlaywrightSamplerKt.waitUntilStateToString;

public class PlaywrightSamplerGUI extends AbstractSamplerGui {
    private final JTextField urlTextField = new JTextField("https://playwright.dev");
    private final JTextField refererTextField = new JTextField();
    private final JSpinner timeoutField = new JSpinner();
    private final JComboBox waitUntilComboBox = new JComboBox(WaitUntilState.values());

    public PlaywrightSamplerGUI() {
        super();
        setLayout(new BorderLayout(0, 5));
        setBorder(makeBorder());

        Box box = Box.createVerticalBox();
        box.add(makeTitlePanel());
        init();
    }

    private void init(){
        VerticalPanel propsPanel = new VerticalPanel();
        propsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
                "Playwright Request")); // $NON-NLS-1$

        // Test URL
        JPanel urlPanel = new JPanel(new BorderLayout(5, 0));
        JLabel urlLabel = new JLabel("URL");
        urlPanel.add(urlLabel, BorderLayout.WEST);

        urlTextField.setName("Playwright.URL");
        urlLabel.setLabelFor(urlTextField);
        urlPanel.add(urlTextField, BorderLayout.CENTER);

        // wait until..
        JPanel waitUntilPanel = new JPanel(new BorderLayout(5, 0));
        JLabel waitUntilLabel = new JLabel("Wait Until");
        waitUntilPanel.add(waitUntilLabel, BorderLayout.WEST);

        waitUntilComboBox.setName("Playwright.WaitUntil");
        waitUntilLabel.setLabelFor(waitUntilComboBox);
        waitUntilPanel.add(waitUntilComboBox, BorderLayout.CENTER);

        // timeout
        JPanel timeOutPanel = new JPanel(new BorderLayout(5, 0));
        JLabel timeoutLabel = new JLabel("Timeout");
        timeOutPanel.add(timeoutLabel, BorderLayout.WEST);

        timeoutField.setName("Playwright.Timeout");
        timeoutLabel.setLabelFor(timeoutField);
        timeOutPanel.add(timeoutField, BorderLayout.CENTER);

        // referer
        JPanel refererPanel = new JPanel(new BorderLayout(5, 0));
        JLabel refererLabel = new JLabel("Referer");
        refererPanel.add(refererLabel, BorderLayout.WEST);

        refererTextField.setName("Playwright.Referer");
        refererLabel.setLabelFor(refererTextField);
        refererPanel.add(refererTextField, BorderLayout.CENTER);

        propsPanel.add(urlPanel);
        propsPanel.add(waitUntilPanel);
        propsPanel.add(timeOutPanel);
        propsPanel.add(refererPanel);

        VerticalPanel integrationPanel = new VerticalPanel();
        integrationPanel.add(propsPanel);
        add(integrationPanel, BorderLayout.CENTER);
    }

    @Override
    public String getLabelResource() {
        return "Playwright Sampler";
    }

    @Override
    public String getStaticLabel() {
        return getLabelResource();
    }

    @Override
    public TestElement createTestElement() {
        PlaywrightSampler sampler = new PlaywrightSampler();
        modifyTestElement(sampler);
        return sampler;
    }

    @Override
    public void modifyTestElement(TestElement element) {
        super.configureTestElement(element);
        if (element instanceof PlaywrightSampler) {
            PlaywrightSampler sampler = (PlaywrightSampler) element;
            sampler.setUrl(urlTextField.getText());
            sampler.setTimeout((Integer) timeoutField.getValue());
            sampler.setWaitUntilState(waitUntilStateFromString(waitUntilComboBox.getSelectedItem().toString()));
        }
    }

    @Override
    public void configure(TestElement element) {
        super.configure(element);
        if (element instanceof PlaywrightSampler) {
            PlaywrightSampler sampler = (PlaywrightSampler) element;
            urlTextField.setText(sampler.getUrl());
            timeoutField.setValue(sampler.getTimeout());
            waitUntilComboBox.setSelectedItem(waitUntilStateToString(sampler.getWaitUntilState()));
        }
    }
}
