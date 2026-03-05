package com.automation.runners;

import org.junit.platform.suite.api.*;

/** Runner de smoke tests — verificação rápida de sanidade no CI/CD. */
@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(key = "cucumber.plugin", value = "pretty, " +
        "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:, " +
        "com.automation.utils.ExtentReportManager")
@ConfigurationParameter(key = "cucumber.filter.tags", value = "@smoke")
@ConfigurationParameter(key = "cucumber.publish.quiet", value = "true")
public class SmokeTestRunner {
}
