package com.automation.hooks;

import com.automation.drivers.DriverManager;
import com.automation.utils.ScreenshotUtils;
import io.cucumber.java.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Cucumber Hooks: setup, teardown e coleta de evidências.
 *
 * Java 17: text blocks para formatação do banner de log.
 */
public class Hooks {

    private static final Logger log = LoggerFactory.getLogger(Hooks.class);

    @BeforeAll
    public static void suiteSetUp() {
        ScreenshotUtils.createScreenshotDirectory();
        log.info("""
                ╔══════════════════════════════════════════════════════╗
                ║       📱 Mobile Automation — Suite Iniciada          ║
                ╚══════════════════════════════════════════════════════╝
                """);
    }

    @Before
    public void scenarioSetUp(Scenario scenario) {
        log.info("""
                ┌──────────────────────────────────────────────────────
                │ ▶ Iniciando: {}
                │   Tags: {}
                └──────────────────────────────────────────────────────
                """, scenario.getName(), scenario.getSourceTagNames());

        DriverManager.initDriver();
    }

    @After
    public void scenarioTearDown(Scenario scenario) {
        log.info("◀ Finalizando: {} — Status: {}", scenario.getName(), scenario.getStatus());

        if (scenario.isFailed()) {
            log.warn("⚠ Cenário FALHOU — capturando screenshot como evidência");
            var screenshot = ScreenshotUtils.capture();
            if (screenshot != null) {
                scenario.attach(screenshot, "image/png",
                        "📸 Evidência de falha — %s".formatted(scenario.getName()));
                ScreenshotUtils.saveToFile(screenshot, scenario.getName());
            }
        }

        DriverManager.quitDriver();
    }

    @AfterAll
    public static void suiteTearDown() {
        log.info("""
                ╔══════════════════════════════════════════════════════╗
                ║       ✅ Mobile Automation — Suite Finalizada        ║
                ║       Relatórios disponíveis em: reports/            ║
                ╚══════════════════════════════════════════════════════╝
                """);
    }
}
