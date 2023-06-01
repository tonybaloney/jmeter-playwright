package com.microsoft.playwright.jmeter.junit
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.readValue
class JUnitDeserializer {
    private val mapper = XmlMapper()

    fun deserialize(input: String) : testsuites {
        return mapper.readValue<testsuites>(input)
    }
}