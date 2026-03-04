package com.automation.pages;

import com.automation.config.ConfigLoader;
import com.automation.drivers.DriverManager;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

/**
 * Classe base para todos os Page Objects.
 *
 * Java 17 features utilizadas:
 * - {@code var} para inferência de tipo
 * - String.formatted() para mensagens de erro descritivas
 * - Métodos utilitários com nomes expressivos
 */
public abstract class BasePage {

    protected final Logger log = LoggerFactory.getLogger(getClass());
    protected final AppiumDriver driver;
    protected final WebDriverWait wait;

    protected BasePage() {
        this.driver = DriverManager.getDriver();
        var timeout = ConfigLoader.getInstance().getConfig().explicitWait();
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(timeout));
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    // ── Esperas ───────────────────────────────────────────────────

    protected WebElement waitVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected WebElement waitVisible(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    protected WebElement waitClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    protected WebElement waitClickable(WebElement element) {
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    // ── Ações ─────────────────────────────────────────────────────

    protected void tap(By locator) {
        log.debug("Tap → {}", locator);
        waitClickable(locator).click();
    }

    protected void tap(WebElement element) {
        log.debug("Tap → elemento");
        waitClickable(element).click();
    }

    protected void fill(WebElement element, String text) {
        log.debug("Fill → '{}'", text);
        var el = waitVisible(element);
        el.clear();
        el.sendKeys(text);
    }

    protected void fill(By locator, String text) {
        log.debug("Fill '{}' → {}", text, locator);
        var el = waitVisible(locator);
        el.clear();
        el.sendKeys(text);
    }

    // ── Leituras ──────────────────────────────────────────────────

    protected String readText(By locator) {
        return waitVisible(locator).getText();
    }

    protected String readText(WebElement element) {
        return waitVisible(element).getText();
    }

    // ── Verificações ──────────────────────────────────────────────

    protected boolean isVisible(WebElement element) {
        try {
            return waitVisible(element).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    protected boolean isVisible(By locator) {
        try {
            return waitVisible(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    protected void waitInvisible(By locator) {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }
}
