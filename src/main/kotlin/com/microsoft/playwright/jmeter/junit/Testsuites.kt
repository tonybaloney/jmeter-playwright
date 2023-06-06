package com.microsoft.playwright.jmeter.junit

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText

@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class property (
        @JsonProperty("name") var name: String? = "",
        @JsonProperty("value") var value: String? = ""
)

@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class properties (
        @JsonProperty("property") var property: List<property>? = null
)

@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class testcase (
        @JsonProperty("name") var name: String = "",
) {
    var skipped: Int = 0
    @JsonProperty("error")
    var error: List<Error> = emptyList()

    @JacksonXmlElementWrapper(useWrapping = false)
    @JsonProperty("failure")
    var failure: List<failure> = emptyList()
    @JsonProperty("system-out")
    var systemOut: List<String> = emptyList()
    @JsonProperty("system-err")
    var systemErr: List<String> = emptyList()
    @JsonProperty("assertions")
    var assertions: String = ""
    @JsonProperty("time")
    var time: String = ""
    @JsonProperty("classname")
    var classname: String = ""
    @JsonProperty("status")
    var status: String = ""
    var properties: properties? = null
}

@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class testsuite (
        @JsonProperty("name") var name: String = "",
    ) {
    var properties: properties? = null
    @JacksonXmlElementWrapper(useWrapping = false)
    var testcase: List<testcase> = emptyList()
    var tests: Int = 0
    @JsonProperty("system-out")
    var systemOut: String = ""
    @JsonProperty("system-err")
    var systemErr: String = ""

    var time: String = ""
    var failures: Int = 0
    var skipped: Int = 0
    var disabled: Int = 0
    var errors: Int = 0
    var timestamp: String = ""
    var hostname: String = ""
    var id: String = ""

    @JsonProperty("package")
    var _package: String = ""
}

@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class testsuites (
        @JsonProperty("name") var name: String = "",
        @JsonProperty("tests") var tests: Int = 0) {
    var id: String = ""
    @JacksonXmlElementWrapper(useWrapping = false)
    var testsuite: List<testsuite> = emptyList()
    var failures: Int = 0
    var skipped: Int = 0
    var disabled: Int = 0
    var errors: Int = 0
    var time: String = ""
}

@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class error (
    var content: String = "",
    var type: String = "",
    var message: String = ""
)

@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class failure (
        @JsonProperty("type")var type: String = "",
        @JsonProperty("message")var message: String = "" ) {
    @JacksonXmlCData
    @JacksonXmlText
    var content: String = ""
}
