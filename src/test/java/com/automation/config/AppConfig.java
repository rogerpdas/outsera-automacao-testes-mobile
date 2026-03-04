package com.automation.config;

/**
 * Immutable configuration record — Java 17 Records.
 * Substitui classes de configuração verbosas com getters/setters.
 * Records são imutáveis por padrão, thread-safe e têm equals/hashCode/toString automáticos.
 */
public record AppConfig(
        // Appium
        String appiumUrl,
        int implicitWait,
        int explicitWait,

        // Device/Platform
        String platformName,
        String deviceName,
        String platformVersion,
        String appPath,
        String automationName,

        // Credentials
        String validUsername,
        String validPassword,

        // Report paths
        String reportOutputPath,
        String screenshotsPath
) {

    /**
     * Compact constructor com validação — Java 17.
     * Garante que campos obrigatórios não sejam nulos.
     */
    public AppConfig {
        if (appiumUrl == null || appiumUrl.isBlank())
            throw new IllegalArgumentException("appiumUrl não pode ser nulo ou vazio");
        if (platformName == null || platformName.isBlank())
            throw new IllegalArgumentException("platformName não pode ser nulo ou vazio");
        if (appPath == null || appPath.isBlank())
            throw new IllegalArgumentException("appPath não pode ser nulo ou vazio");
    }
}
