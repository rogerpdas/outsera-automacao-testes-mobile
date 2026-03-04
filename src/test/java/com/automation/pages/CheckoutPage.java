package com.automation.pages;

import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.WebElement;

/**
 * Page Object da tela de Checkout (formulário).
 * Cobre a Tarefa 2: preenchimento de formulário e envio.
 *
 * Java 17: record interno {@code FormData} para encapsular os dados do formulário.
 */
public class CheckoutPage extends BasePage {

    /**
     * Record que representa os dados do formulário de checkout.
     * Imutável, com equals/hashCode/toString automáticos — Java 17.
     */
    public record FormData(String firstName, String lastName, String postalCode) {
        /** Compact constructor com validação de campos obrigatórios */
        public FormData {
            if (firstName  == null) firstName  = "";
            if (lastName   == null) lastName   = "";
            if (postalCode == null) postalCode = "";
        }
    }

    // ── Step 1: Dados pessoais ────────────────────────────────────

    @AndroidFindBy(accessibility = "test-First Name")
    @iOSXCUITFindBy(accessibility = "First Name")
    private WebElement firstNameField;

    @AndroidFindBy(accessibility = "test-Last Name")
    @iOSXCUITFindBy(accessibility = "Last Name")
    private WebElement lastNameField;

    @AndroidFindBy(accessibility = "test-Zip/Postal Code")
    @iOSXCUITFindBy(accessibility = "Zip/Postal Code")
    private WebElement postalCodeField;

    @AndroidFindBy(accessibility = "test-CONTINUE")
    @iOSXCUITFindBy(accessibility = "CONTINUE")
    private WebElement continueButton;

    @AndroidFindBy(accessibility = "test-CANCEL")
    @iOSXCUITFindBy(accessibility = "CANCEL")
    private WebElement cancelButton;

    @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc='test-Error message']/android.widget.TextView")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeStaticText[@name='Error message']")
    private WebElement errorMessage;

    // ── Step 2: Resumo do pedido ──────────────────────────────────

    @AndroidFindBy(xpath = "//android.widget.TextView[@text='CHECKOUT: OVERVIEW']")
    @iOSXCUITFindBy(accessibility = "CHECKOUT: OVERVIEW")
    private WebElement overviewTitle;

    @AndroidFindBy(accessibility = "test-FINISH")
    @iOSXCUITFindBy(accessibility = "FINISH")
    private WebElement finishButton;

    // ── Step 3: Confirmação ───────────────────────────────────────

    @AndroidFindBy(xpath = "//android.widget.TextView[@text='CHECKOUT: COMPLETE!']")
    @iOSXCUITFindBy(accessibility = "CHECKOUT: COMPLETE!")
    private WebElement completeTitle;

    @AndroidFindBy(accessibility = "test-BACK HOME")
    @iOSXCUITFindBy(accessibility = "BACK HOME")
    private WebElement backHomeButton;

    @AndroidFindBy(xpath = "//android.widget.TextView[contains(@text,'THANK YOU')]")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeStaticText[contains(@name,'THANK YOU')]")
    private WebElement thankYouMessage;

    // ── Ações ─────────────────────────────────────────────────────

    public CheckoutPage enterFirstName(String value) {
        fill(firstNameField, value);
        return this;
    }

    public CheckoutPage enterLastName(String value) {
        fill(lastNameField, value);
        return this;
    }

    public CheckoutPage enterPostalCode(String value) {
        fill(postalCodeField, value);
        return this;
    }

    /** Preenchimento via record FormData */
    public CheckoutPage fillForm(FormData data) {
        log.info("Preenchendo formulário: {} {} — CEP {}", data.firstName(), data.lastName(), data.postalCode());
        enterFirstName(data.firstName());
        enterLastName(data.lastName());
        enterPostalCode(data.postalCode());
        return this;
    }

    public CheckoutPage tapContinue() {
        log.info("Tocando em CONTINUE");
        scrollToAndTap(continueButton);
        return this;
    }

    public CheckoutPage tapFinish() {
        log.info("Confirmando pedido (FINISH)");
        scrollToAndTap(finishButton);
        return this;
    }

    public CartPage tapCancel() {
        tap(cancelButton);
        return new CartPage();
    }

    public ProductsPage tapBackHome() {
        tap(backHomeButton);
        return new ProductsPage();
    }

    // ── Validações ────────────────────────────────────────────────

    public boolean isOnStepOne()          { return isVisible(firstNameField); }
    public boolean isOnOverview()         { return isVisible(overviewTitle); }
    public boolean isOrderComplete()      { return isVisible(completeTitle); }
    public boolean isThankYouVisible()    { return isVisible(thankYouMessage); }
    public boolean isErrorVisible()       { return isVisible(errorMessage); }
    public String  getErrorMessage()      { return readText(errorMessage); }
    public String  getThankYouMessage()   { return readText(thankYouMessage); }
}
