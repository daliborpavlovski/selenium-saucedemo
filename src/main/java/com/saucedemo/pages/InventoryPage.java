package com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;
import java.util.stream.Collectors;

public class InventoryPage extends BasePage {

    // Locators
    private static final By PAGE_TITLE          = By.cssSelector(".title");
    private static final By PRODUCT_NAMES       = By.cssSelector(".inventory_item_name");
    private static final By PRODUCT_PRICES      = By.cssSelector(".inventory_item_price");
    private static final By PRODUCT_CARDS       = By.cssSelector(".inventory_item");
    private static final By SORT_DROPDOWN       = By.cssSelector("[data-test='product-sort-container']");
    private static final By CART_ICON           = By.cssSelector(".shopping_cart_link");
    private static final By CART_BADGE          = By.cssSelector(".shopping_cart_badge");
    private static final By BURGER_MENU_BUTTON  = By.id("react-burger-menu-btn");
    private static final By LOGOUT_LINK         = By.id("logout_sidebar_link");
    private static final By ADD_TO_CART_BUTTONS = By.cssSelector("[data-test^='add-to-cart']");

    public InventoryPage(WebDriver driver) {
        super(driver);
    }

    public String getHeading() {
        return getText(PAGE_TITLE);
    }

    public boolean isOnInventoryPage() {
        return getCurrentUrl().contains("/inventory.html");
    }

    public List<String> getProductNames() {
        return waitForAllVisible(PRODUCT_NAMES)
                .stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    public List<Double> getProductPrices() {
        return waitForAllVisible(PRODUCT_PRICES)
                .stream()
                .map(el -> Double.parseDouble(el.getText().replace("$", "")))
                .collect(Collectors.toList());
    }

    public int getProductCount() {
        return waitForAllVisible(PRODUCT_CARDS).size();
    }

    public InventoryPage sortBy(String option) {
        Select sort = new Select(waitForVisible(SORT_DROPDOWN));
        sort.selectByVisibleText(option);
        return this;
    }

    /**
     * Adds a product to the cart by its displayed name.
     * Constructs the data-test attribute dynamically as Sauce Demo uses kebab-case IDs.
     */
    public InventoryPage addToCartByName(String productName) {
        // e.g. "Sauce Labs Backpack" -> "add-to-cart-sauce-labs-backpack"
        String dataTest = "add-to-cart-" + productName.toLowerCase().replace(" ", "-");
        click(By.cssSelector("[data-test='" + dataTest + "']"));
        return this;
    }

    public InventoryPage addAllToCart() {
        // Re-fetch after each click: the button transforms to "Remove" on click,
        // which triggers a React re-render and stales the rest of the fetched list.
        List<WebElement> buttons = driver.findElements(ADD_TO_CART_BUTTONS);
        while (!buttons.isEmpty()) {
            buttons.get(0).click();
            buttons = driver.findElements(ADD_TO_CART_BUTTONS);
        }
        return this;
    }

    public int getCartBadgeCount() {
        if (!isDisplayed(CART_BADGE)) return 0;
        return Integer.parseInt(getText(CART_BADGE));
    }

    public CartPage goToCart() {
        click(CART_ICON);
        return new CartPage(driver);
    }

    public ProductPage openProduct(String productName) {
        click(By.linkText(productName));
        return new ProductPage(driver);
    }

    public LoginPage logout() {
        click(BURGER_MENU_BUTTON);
        WebElement logoutLink = waitForVisible(LOGOUT_LINK);
        scrollIntoView(logoutLink);
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", logoutLink);
        return new LoginPage(driver);
    }
}
