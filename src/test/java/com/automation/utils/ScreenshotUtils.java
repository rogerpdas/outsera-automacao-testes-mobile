package com.automation.utils;

import com.automation.config.ConfigLoader;
import com.automation.drivers.DriverManager;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utilitário para captura e salvamento de screenshots.
 *
 * Java 17 features utilizadas:
 * - {@code java.nio.file.Path} e {@code Files} (API moderna de I/O)
 * - {@code var} para inferência local
 * - String.formatted() para nomes de arquivo
 */
public final class ScreenshotUtils {

    private static final Logger log = LoggerFactory.getLogger(ScreenshotUtils.class);
    private static final DateTimeFormatter TIMESTAMP = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    private ScreenshotUtils() {}

    public static void createScreenshotDirectory() {
        var path = Path.of(ConfigLoader.getInstance().getConfig().screenshotsPath());
        try {
            Files.createDirectories(path);
            log.info("Diretório de screenshots: {}", path.toAbsolutePath());
        } catch (IOException e) {
            log.error("Falha ao criar diretório de screenshots: {}", e.getMessage());
        }
    }

    public static byte[] capture() {
        if (!DriverManager.isInitialized()) {
            log.warn("Driver não inicializado — screenshot não capturado");
            return null;
        }
        try {
            return ((TakesScreenshot) DriverManager.getDriver())
                    .getScreenshotAs(OutputType.BYTES);
        } catch (Exception e) {
            log.error("Falha ao capturar screenshot: {}", e.getMessage());
            return null;
        }
    }

    public static void saveToFile(byte[] screenshot, String scenarioName) {
        if (screenshot == null) return;

        var timestamp     = LocalDateTime.now().format(TIMESTAMP);
        var sanitizedName = scenarioName.replaceAll("[^a-zA-Z0-9]", "_");
        var fileName      = "%s_%s.png".formatted(sanitizedName, timestamp);
        var filePath      = Path.of(ConfigLoader.getInstance().getConfig().screenshotsPath(), fileName);

        try {
            Files.write(filePath, screenshot);
            log.info("Screenshot salvo: {}", filePath.toAbsolutePath());
        } catch (IOException e) {
            log.error("Falha ao salvar screenshot: {}", e.getMessage());
        }
    }
}
