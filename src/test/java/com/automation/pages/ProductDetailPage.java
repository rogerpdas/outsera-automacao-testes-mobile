package com.automation.pages;

import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.WebElement;

// ═══════════════════════════════════════════════════════════════════
// ProductDetailPage
// ═══════════════════════════════════════════════════════════════════

/**
 * Page Object da tela de Detalhe do Produto.
 */
public class ProductDetailPage extends BasePage {

    @AndroidFindBy(accessibility = "test-Description")
    @iOSXCUITFindBy(accessibility = "test-Description")
    private WebElement description;

    @AndroidFindBy(accessibility = "test-Price")
    @iOSXCUITFindBy(accessibility = "test-Price")
    private WebElement price;

    @AndroidFindBy(accessibility = "test-ADD TO CART")
    @iOSXCUITFindBy(accessibility = "ADD TO CART")
    private WebElement addToCartButton;

    @AndroidFindBy(accessibility = "test-REMOVE")
    @iOSXCUITFindBy(accessibility = "REMOVE")
    private WebElement removeButton;

    @AndroidFindBy(accessibility = "test-BACK TO PRODUCTS")
    @iOSXCUITFindBy(accessibility = "BACK TO PRODUCTS")
    private WebElement backButton;

    public ProductDetailPage tapAddToCart() {
        log.info("Adicionando ao carrinho");
        scrollToAndTap(addToCartButton);
        return this;
    }

    public ProductsPage tapBackToProducts() {
        log.info("Voltando para produtos");
        tap(backButton);
        return new ProductsPage();
    }
}
