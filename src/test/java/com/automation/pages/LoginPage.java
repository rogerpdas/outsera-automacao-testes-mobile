package com.automation.pages;

import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.WebElement;

/**
 * Page Object da tela de Login.
 * Locators nativos via {@code @AndroidFindBy} / {@code @iOSXCUITFindBy} para suporte cross-platform.
 */
public class LoginPage extends BasePage {

    @AndroidFindBy(accessibility = "test-Username")
    @iOSXCUITFindBy(accessibility = "Username")
    private WebElement usernameField;

    @AndroidFindBy(accessibility = "test-Password")
    @iOSXCUITFindBy(accessibility = "Password")
    private WebElement passwordField;

    @AndroidFindBy(accessibility = "test-LOGIN")
    @iOSXCUITFindBy(accessibility = "LOGIN")
    private WebElement loginButton;

    @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc='test-Error message']/android.widget.TextView")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeStaticText[@name='Error message']")
    private WebElement errorMessage;

    // ── Ações ─────────────────────────────────────────────────────

    public LoginPage enterUsername(String username) {
        log.info("Preenchendo username: {}", username);
        fill(usernameField, username);
        return this;
    }

    public LoginPage enterPassword(String password) {
        log.info("Preenchendo password");
        fill(passwordField, password);
        return this;
    }

    /** Login bem-sucedido → navega para a tela de Produtos */
    public ProductsPage tapLogin() {
        log.info("Tocando em LOGIN");
        tap(loginButton);
        return new ProductsPage();
    }

    /** Login com erro esperado → permanece na tela de Login */
    public LoginPage tapLoginExpectingError() {
        log.info("Tocando em LOGIN (erro esperado)");
        tap(loginButton);
        return this;
    }

    /** Fluxo completo de login */
    public ProductsPage doLogin(String username, String password) {
        return enterUsername(username)
                .enterPassword(password)
                .tapLogin();
    }

    // ── Validações ────────────────────────────────────────────────

    public boolean isOnLoginPage()      { return isVisible(usernameField); }
    public boolean isErrorVisible()     { return isVisible(errorMessage); }
    public String  getErrorMessage()    { return readText(errorMessage); }
}
