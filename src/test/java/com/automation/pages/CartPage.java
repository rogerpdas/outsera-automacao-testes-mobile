package com.automation.pages;

import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.WebElement;

import java.util.List;

/** Page Object da tela do Carrinho. */
public class CartPage extends BasePage {

    @AndroidFindBy(xpath = "//android.widget.TextView[@text='YOUR CART']")
    @iOSXCUITFindBy(accessibility = "YOUR CART")
    private WebElement cartTitle;

    @AndroidFindBy(accessibility = "test-CHECKOUT")
    @iOSXCUITFindBy(accessibility = "CHECKOUT")
    private WebElement checkoutButton;

    @AndroidFindBy(accessibility = "test-CONTINUE SHOPPING")
    @iOSXCUITFindBy(accessibility = "CONTINUE SHOPPING")
    private WebElement continueShoppingButton;

    @AndroidFindBy(accessibility = "test-Item")
    @iOSXCUITFindBy(accessibility = "test-Item")
    private List<WebElement> cartItems;

    public CheckoutPage tapCheckout() {
        log.info("Iniciando checkout");
        scrollToAndTap(checkoutButton);
        return new CheckoutPage();
    }

    public ProductsPage tapContinueShopping() {
        log.info("Continuando comprando");
        scrollToAndTap(continueShoppingButton);
        return new ProductsPage();
    }

    public boolean isOnCartPage()   { return isVisible(cartTitle); }
    public int     getItemCount()   { return cartItems.size(); }
}
