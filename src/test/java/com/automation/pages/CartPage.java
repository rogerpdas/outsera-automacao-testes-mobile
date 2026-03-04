package com.automation.pages;

import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.WebElement;

/**
 * Page Object da tela do Carrinho.
 * Serve como ponto de passagem para o Formulário de Cadastro.
 */
public class CartPage extends BasePage {

    @AndroidFindBy(accessibility = "test-CHECKOUT")
    @iOSXCUITFindBy(accessibility = "CHECKOUT")
    private WebElement botaoCheckout;

    public FormularioCadastroPage tapCheckout() {
        log.info("Abrindo formulário de cadastro via checkout");
        scrollToAndTap(botaoCheckout);
        return new FormularioCadastroPage();
    }
}
