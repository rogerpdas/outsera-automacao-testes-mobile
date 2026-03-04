package com.automation.pages;

import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.WebElement;

/**
 * Page Object da tela do Carrinho.
 * Serve como ponto de passagem para o Formulário de Cadastro.
 */
public class CartPage extends BasePage {

    @AndroidFindBy(xpath = "//android.widget.TextView[@text='YOUR CART']")
    @iOSXCUITFindBy(accessibility = "YOUR CART")
    private WebElement tituloPagina;

    @AndroidFindBy(accessibility = "test-CHECKOUT")
    @iOSXCUITFindBy(accessibility = "CHECKOUT")
    private WebElement botaoCheckout;

    @AndroidFindBy(accessibility = "test-CONTINUE SHOPPING")
    @iOSXCUITFindBy(accessibility = "CONTINUE SHOPPING")
    private WebElement botaoContinuarComprando;

    @AndroidFindBy(accessibility = "test-Item")
    @iOSXCUITFindBy(accessibility = "test-Item")
    private java.util.List<WebElement> itens;

    public FormularioCadastroPage tapCheckout() {
        log.info("Abrindo formulário de cadastro via checkout");
        scrollToAndTap(botaoCheckout);
        return new FormularioCadastroPage();
    }

    public ProductsPage tapContinuarComprando() {
        log.info("Voltando para produtos");
        tap(botaoContinuarComprando);
        return new ProductsPage();
    }

    public boolean isOnCartPage()  { return isVisible(tituloPagina); }
    public int     getItemCount()  { return itens.size(); }
}
