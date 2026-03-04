package com.automation.config;

import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * Sealed interface representando as plataformas suportadas — Java 17 Sealed Classes.
 *
 * Com sealed classes, o compilador garante que apenas Android e Ios
 * são implementações válidas, permitindo pattern matching exhaustivo
 * em switch expressions sem necessidade de default clause.
 */
public sealed interface Platform permits Platform.Android, Platform.Ios {

    /** Constrói as DesiredCapabilities específicas da plataforma */
    DesiredCapabilities buildCapabilities(AppConfig config);

    /** Nome canônico da plataforma */
    String name();

    // ── Implementações ────────────────────────────────────────────

    record Android() implements Platform {

        @Override
        public String name() { return "android"; }

        @Override
        public DesiredCapabilities buildCapabilities(AppConfig config) {
            var caps = new DesiredCapabilities();
            caps.setCapability("platformName",              "Android");
            caps.setCapability("appium:deviceName",                config.deviceName());
            caps.setCapability("appium:platformVersion",           config.platformVersion());
            caps.setCapability("appium:automationName",            config.automationName());
            caps.setCapability("appium:app",                       resolveAppPath(config.appPath()));
            caps.setCapability("appium:noReset",                   false);
            caps.setCapability("appium:newCommandTimeout",         300);
            caps.setCapability("appium:appWaitActivity",           "*");
            caps.setCapability("appium:uiautomator2ServerLaunchTimeout", 60_000);
            caps.setCapability("appium:androidDeviceReadyTimeout", 60);
            return caps;
        }
    }

    record Ios() implements Platform {

        @Override
        public String name() { return "ios"; }

        @Override
        public DesiredCapabilities buildCapabilities(AppConfig config) {
            var caps = new DesiredCapabilities();
            caps.setCapability("platformName",    "iOS");
            caps.setCapability("appium:deviceName",      config.deviceName());
            caps.setCapability("appium:platformVersion", config.platformVersion());
            caps.setCapability("appium:automationName",  config.automationName());
            caps.setCapability("appium:app",             resolveAppPath(config.appPath()));
            caps.setCapability("appium:noReset",         false);
            caps.setCapability("appium:startIWDP",       true);
            caps.setCapability("appium:wdaLaunchTimeout", 60_000);
            return caps;
        }
    }

    // ── Factory method ────────────────────────────────────────────

    /**
     * Cria a instância correta a partir de uma string.
     * Switch expression com arrow cases — Java 17.
     */
    static Platform of(String platformName) {
        return switch (platformName.toLowerCase().trim()) {
            case "android" -> new Android();
            case "ios"     -> new Ios();
            default        -> throw new IllegalArgumentException(
                    "Plataforma não suportada: '%s'. Use 'android' ou 'ios'.".formatted(platformName)
            );
        };
    }

    private static String resolveAppPath(String appPath) {
        return System.getProperty("user.dir") + "/" + appPath;
    }
}
