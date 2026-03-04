package com.automation.runners;

import org.junit.platform.suite.api.*;

/**
 * Runner principal — executa toda a suite de regressão.
 * Para filtrar por tags: {@code mvn test -Dcucumber.filter.tags="@smoke"}
 */
@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(
    key = "cucumber.plugin",
    value = "pretty, " +
            "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
)
@ConfigurationParameter(key = "cucumber.publish.quiet", value = "true")
public class TestRunner {}
