# jmeter-playwright

experimental JMeter Plugin to launch playwright from a custom thread group.

![Working screenshot](screenshot-playwright.png)

## Installation

Copy the JAR file from this site's build into your JMeter `lib/ext` directory. Requires JMeter 5+.

## Components

### Playwright Thread Group

Replaces the builtin Thread Group with similar controls to set the number of users. Each thread represents a Browser Context in Playwright. 
All samples and assertions inside the group will be done in parallel for the number of users you configure.
Set loops to repeat the steps in order.

### Playwright Sampler

This sampler will sample the navigation to a URL for the load time, or the time it takes for the DOM to complete, or for all network activity to cease (configurable).

Samples will be recorded using the complete time.

### Playwright Click Post Processor

Configure a selector (the element to find) and then it will click on that element.

### Playwright Fill Post Processor

Configure a selector and then the value, it will type the text into that box.

### Playwright Assertion

Assert that an element on the page meets the criteria (is visible, is hidden etc).

Optionally, take a screenshot if the assertion fails. Check the logs for the path to the screenshot. 

