package com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckoutCompletePage extends BasePage {

    private static final By COMPLETE_HEADER  = By.cssSelector(".complete-header");
    private static final By COMPLETE_TEXT    = By.cssSelector(".complete-text");
    private static final By BACK_HOME_BUTTON = By.id("back-to-products");
    private static final By ORDER_CONFIRMATION_IMAGE = By.cssSelector(".pony_express");

    public CheckoutCompletePage(WebDriver driver) {
        super(driver);
    }

    public boolean isOnCheckoutCompletePage() {
        try {
            wait.until(org.openqa.selenium.support.ui.ExpectedConditions
                    .urlContains("/checkout-complete.html"));
            return true;
        } catch (org.openqa.selenium.TimeoutException e) {
            return false;
        }
    }

    public String getConfirmationHeader() {
        return getText(COMPLETE_HEADER);
    }

    public String getConfirmationText() {
        return getText(COMPLETE_TEXT);
    }

    public boolean isOrderConfirmationImageVisible() {
        return isDisplayed(ORDER_CONFIRMATION_IMAGE);
    }

    public InventoryPage backToHome() {
        click(BACK_HOME_BUTTON);
        return new InventoryPage(driver);
    }
}
