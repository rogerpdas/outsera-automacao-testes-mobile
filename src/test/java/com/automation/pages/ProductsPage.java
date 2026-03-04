package com.automation.pages;

import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Page Object da tela de Produtos (Home após login).
 */
public class ProductsPage extends BasePage {

    @AndroidFindBy(xpath = "//android.widget.TextView[@text='PRODUCTS']")
    @iOSXCUITFindBy(accessibility = "PRODUCTS")
    private WebElement productsTitle;

    @AndroidFindBy(accessibility = "test-Menu")
    @iOSXCUITFindBy(accessibility = "Menu")
    private WebElement menuButton;

    @AndroidFindBy(accessibility = "test-LOGOUT")
    @iOSXCUITFindBy(accessibility = "LOGOUT")
    private WebElement logoutButton;

    @AndroidFindBy(accessibility = "test-Cart")
    @iOSXCUITFindBy(accessibility = "test-Cart")
    private WebElement cartIcon;

    @AndroidFindBy(accessibility = "test-Item")
    @iOSXCUITFindBy(accessibility = "test-Item")
    private List<WebElement> productItems;

    // ── Ações ─────────────────────────────────────────────────────

    public ProductDetailPage tapFirstProduct() {
        log.info("Tocando no primeiro produto");
        if (productItems.isEmpty())
            throw new IllegalStateException("Nenhum produto listado na tela");
        tap(productItems.get(0));
        return new ProductDetailPage();
    }

    public ProductDetailPage tapProductByIndex(int index) {
        log.info("Tocando no produto de índice {}", index);
        tap(productItems.get(index));
        return new ProductDetailPage();
    }

    public ProductDetailPage tapProductByName(String name) {
        log.info("Tocando no produto: {}", name);
        var locator = By.xpath("//android.widget.TextView[@text='%s']".formatted(name));
        tap(locator);
        return new ProductDetailPage();
    }

    public LoginPage doLogout() {
        log.info("Realizando logout");
        tap(menuButton);
        tap(logoutButton);
        return new LoginPage();
    }

    public CartPage tapCartIcon() {
        log.info("Abrindo carrinho");
        tap(cartIcon);
        return new CartPage();
    }

    // ── Validações ────────────────────────────────────────────────

    public boolean isOnProductsPage() {
        return isVisible(productsTitle);
    }

    public String getPageTitle() {
        return readText(productsTitle);
    }

    public int getProductCount() {
        return productItems.size();
    }
}
