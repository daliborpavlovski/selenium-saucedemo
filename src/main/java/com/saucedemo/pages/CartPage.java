package com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

public class CartPage extends BasePage {

    private static final By PAGE_TITLE          = By.cssSelector(".title");
    private static final By CART_ITEMS          = By.cssSelector(".cart_item");
    private static final By ITEM_NAMES          = By.cssSelector(".inventory_item_name");
    private static final By ITEM_PRICES         = By.cssSelector(".inventory_item_price");
    private static final By REMOVE_BUTTONS      = By.cssSelector("[data-test^='remove']");
    private static final By CONTINUE_SHOPPING   = By.id("continue-shopping");
    private static final By CHECKOUT_BUTTON     = By.id("checkout");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public boolean isOnCartPage() {
        return getCurrentUrl().contains("/cart.html");
    }

    public String getHeading() {
        return getText(PAGE_TITLE);
    }

    public int getItemCount() {
        waitForVisible(PAGE_TITLE);
        return driver.findElements(CART_ITEMS).size();
    }

    public List<String> getItemNames() {
        waitForVisible(PAGE_TITLE);
        return driver.findElements(ITEM_NAMES)
                .stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    public List<Double> getItemPrices() {
        waitForVisible(PAGE_TITLE);
        return driver.findElements(ITEM_PRICES)
                .stream()
                .map(el -> Double.parseDouble(el.getText().replace("$", "")))
                .collect(Collectors.toList());
    }

    /**
     * Removes a cart item by its displayed name.
     * Constructs the data-test attribute the same way Sauce Demo does.
     */
    public CartPage removeItemByName(String productName) {
        String dataTest = "remove-" + productName.toLowerCase().replace(" ", "-");
        click(By.cssSelector("[data-test='" + dataTest + "']"));
        return this;
    }

    public CartPage removeAllItems() {
        // Re-fetch after each click: React re-renders the list on every removal,
        // making previously fetched WebElement references stale.
        List<WebElement> buttons = driver.findElements(REMOVE_BUTTONS);
        while (!buttons.isEmpty()) {
            buttons.get(0).click();
            buttons = driver.findElements(REMOVE_BUTTONS);
        }
        return this;
    }

    public boolean isCartEmpty() {
        return getItemCount() == 0;
    }

    public InventoryPage continueShopping() {
        click(CONTINUE_SHOPPING);
        return new InventoryPage(driver);
    }

    public CheckoutInfoPage proceedToCheckout() {
        click(CHECKOUT_BUTTON);
        return new CheckoutInfoPage(driver);
    }
}
