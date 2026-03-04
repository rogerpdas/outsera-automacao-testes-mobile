package com.automation.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

/**
 * Singleton responsável por carregar e fornecer a configuração dos testes.
 *
 * Java 17 features utilizadas:
 * - {@code var} para inferência de tipo local
 * - Text blocks para log de diagnóstico
 * - Pattern matching com instanceof (preview)
 * - Optional para valores opcionais sem null checks
 */
public final class ConfigLoader {

    private static final Logger log = LoggerFactory.getLogger(ConfigLoader.class);
    private static volatile ConfigLoader instance;

    private final AppConfig config;

    private ConfigLoader() {
        this.config = loadFromYaml();
    }

    /** Double-checked locking thread-safe singleton */
    public static ConfigLoader getInstance() {
        if (instance == null) {
            synchronized (ConfigLoader.class) {
                if (instance == null) {
                    instance = new ConfigLoader();
                }
            }
        }
        return instance;
    }

    public AppConfig getConfig() {
        return config;
    }

    @SuppressWarnings("unchecked")
    private AppConfig loadFromYaml() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("config.yaml")) {

            if (inputStream == null) {
                throw new IllegalStateException("config.yaml não encontrado em resources");
            }

            var mapper = new ObjectMapper(new YAMLFactory());
            var raw = mapper.readValue(inputStream, Map.class);

            // ── Appium ──────────────────────────────────────────────
            var appium = (Map<String, Object>) raw.get("appium");
            var appiumUrl   = prop("appiumUrl",   (String) appium.get("url"));
            var implicitWait  = (int) appium.get("implicitWait");
            var explicitWait  = (int) appium.get("explicitWait");

            // ── Platform (Android/iOS) ──────────────────────────────
            var platform = prop("platform", "android").toLowerCase();
            var platformCfg = (Map<String, Object>) raw.get(platform);

            var platformName   = (String) platformCfg.get("platformName");
            var deviceName     = prop("deviceName",      (String) platformCfg.get("deviceName"));
            var platformVersion = prop("platformVersion", (String) platformCfg.get("platformVersion"));
            var appPath        = prop("appPath",         (String) platformCfg.get("app"));
            var automationName = (String) platformCfg.get("automationName");

            // ── Credentials ─────────────────────────────────────────
            var credentials = (Map<String, Object>) raw.get("credentials");
            var validUser   = (Map<String, Object>) credentials.get("validUser");
            var username    = (String) validUser.get("username");
            var password    = (String) validUser.get("password");

            // ── Report ───────────────────────────────────────────────
            var report          = (Map<String, Object>) raw.get("report");
            var reportOutputPath = (String) report.get("outputPath");
            var screenshotsPath  = (String) report.get("screenshotsPath");

            var cfg = new AppConfig(
                    appiumUrl, implicitWait, explicitWait,
                    platformName, deviceName, platformVersion, appPath, automationName,
                    username, password,
                    reportOutputPath, screenshotsPath
            );

            // ── Text block para log de diagnóstico ───────────────────
            log.info("""
                    ╔══════════════════════════════════════════╗
                    ║         Configuração carregada           ║
                    ╠══════════════════════════════════════════╣
                    ║  Platform   : {}
                    ║  Device     : {}
                    ║  Appium URL : {}
                    ╚══════════════════════════════════════════╝
                    """,
                    cfg.platformName(), cfg.deviceName(), cfg.appiumUrl());

            return cfg;

        } catch (Exception e) {
            log.error("Falha ao carregar configuração: {}", e.getMessage());
            throw new RuntimeException("Configuração falhou ao inicializar", e);
        }
    }

    /**
     * Retorna system property se definida, senão usa o valor padrão do YAML.
     * Permite override via {@code -Dchave=valor} no Maven/CI.
     */
    private static String prop(String key, String defaultValue) {
        return Optional.ofNullable(System.getProperty(key))
                .filter(v -> !v.isBlank())
                .orElse(defaultValue);
    }
}
