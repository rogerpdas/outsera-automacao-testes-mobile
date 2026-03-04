package com.automation.drivers;

import com.automation.config.AppConfig;
import com.automation.config.ConfigLoader;
import com.automation.config.Platform;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

/**
 * Gerenciador thread-safe do driver Appium via ThreadLocal.
 *
 * Java 17 features utilizadas:
 * - {@code var} para inferência de tipo local
 * - Switch expression com Pattern Matching (sealed Platform)
 * - String.formatted() no lugar de String.format()
 * - Records de AppConfig acessados via método direto (sem get prefix)
 */
public final class DriverManager {

    private static final Logger log = LoggerFactory.getLogger(DriverManager.class);
    private static final ThreadLocal<AppiumDriver> DRIVER_THREAD_LOCAL = new ThreadLocal<>();

    private DriverManager() {}

    // ── Acesso ────────────────────────────────────────────────────

    public static AppiumDriver getDriver() {
        var driver = DRIVER_THREAD_LOCAL.get();
        if (driver == null) {
            throw new IllegalStateException(
                    "Driver não inicializado. Chame initDriver() antes de getDriver()."
            );
        }
        return driver;
    }

    public static boolean isInitialized() {
        return DRIVER_THREAD_LOCAL.get() != null;
    }

    // ── Ciclo de vida ─────────────────────────────────────────────

    public static void initDriver() {
        AppConfig config = ConfigLoader.getInstance().getConfig();
        var platform = Platform.of(config.platformName());

        log.info("Inicializando driver | plataforma={} | device={}",
                platform.name(), config.deviceName());

        try {
            var caps    = platform.buildCapabilities(config);
            var url     = new URL(config.appiumUrl());
            var driver  = createDriver(platform, url, caps);

            driver.manage().timeouts()
                    .implicitlyWait(Duration.ofSeconds(config.implicitWait()));

            DRIVER_THREAD_LOCAL.set(driver);
            log.info("Driver iniciado com sucesso. Session ID: {}", driver.getSessionId());

        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(
                    "URL do Appium inválida: %s".formatted(config.appiumUrl()), e
            );
        }
    }

    public static void quitDriver() {
        var driver = DRIVER_THREAD_LOCAL.get();
        if (driver != null) {
            try {
                driver.quit();
                log.info("Driver encerrado.");
            } catch (Exception e) {
                log.warn("Erro ao encerrar driver: {}", e.getMessage());
            } finally {
                DRIVER_THREAD_LOCAL.remove();
            }
        }
    }

    // ── Criação do driver por plataforma ──────────────────────────

    /**
     * Switch expression com sealed Platform — Java 17.
     * Exaustivo sem necessidade de default.
     */
    private static AppiumDriver createDriver(Platform platform, URL url, DesiredCapabilities caps) {
        if (platform instanceof Platform.Android) {
            return new AndroidDriver(url, caps);
        } else if (platform instanceof Platform.Ios) {
            return new IOSDriver(url, caps);
        }
        throw new IllegalArgumentException("Plataforma desconhecida: " + platform.name());
    }
}
