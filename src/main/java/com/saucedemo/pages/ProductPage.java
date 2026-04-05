package com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ProductPage extends BasePage {

    private static final By PRODUCT_NAME        = By.cssSelector(".inventory_details_name");
    private static final By PRODUCT_DESCRIPTION = By.cssSelector(".inventory_details_desc");
    private static final By PRODUCT_PRICE       = By.cssSelector(".inventory_details_price");
    private static final By ADD_TO_CART_BUTTON  = By.cssSelector("[data-test^='add-to-cart']");
    private static final By REMOVE_BUTTON       = By.cssSelector("[data-test^='remove']");
    private static final By BACK_TO_PRODUCTS_BUTTON = By.id("back-to-products");
    private static final By CART_BADGE          = By.cssSelector(".shopping_cart_badge");

    public ProductPage(WebDriver driver) {
        super(driver);
    }

    public String getProductName() {
        return getText(PRODUCT_NAME);
    }

    public String getDescription() {
        return getText(PRODUCT_DESCRIPTION);
    }

    public double getPrice() {
        return Double.parseDouble(getText(PRODUCT_PRICE).replace("$", ""));
    }

    public ProductPage addToCart() {
        click(ADD_TO_CART_BUTTON);
        return this;
    }

    public ProductPage removeFromCart() {
        click(REMOVE_BUTTON);
        return this;
    }

    public boolean isRemoveButtonVisible() {
        return isDisplayed(REMOVE_BUTTON);
    }

    public boolean isAddToCartButtonVisible() {
        return isDisplayed(ADD_TO_CART_BUTTON);
    }

    public int getCartBadgeCount() {
        if (!isDisplayed(CART_BADGE)) return 0;
        return Integer.parseInt(getText(CART_BADGE));
    }

    public InventoryPage goBackToProducts() {
        click(BACK_TO_PRODUCTS_BUTTON);
        return new InventoryPage(driver);
    }
}
