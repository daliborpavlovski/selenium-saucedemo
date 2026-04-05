package com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckoutInfoPage extends BasePage {

    private static final By FIRST_NAME_INPUT = By.id("first-name");
    private static final By LAST_NAME_INPUT  = By.id("last-name");
    private static final By POSTAL_CODE_INPUT = By.id("postal-code");
    private static final By CONTINUE_BUTTON  = By.id("continue");
    private static final By CANCEL_BUTTON    = By.id("cancel");
    private static final By ERROR_MESSAGE    = By.cssSelector("[data-test='error']");

    public CheckoutInfoPage(WebDriver driver) {
        super(driver);
    }

    public boolean isOnCheckoutInfoPage() {
        return getCurrentUrl().contains("/checkout-step-one.html");
    }

    public CheckoutInfoPage enterFirstName(String firstName) {
        type(FIRST_NAME_INPUT, firstName);
        return this;
    }

    public CheckoutInfoPage enterLastName(String lastName) {
        type(LAST_NAME_INPUT, lastName);
        return this;
    }

    public CheckoutInfoPage enterPostalCode(String postalCode) {
        type(POSTAL_CODE_INPUT, postalCode);
        return this;
    }

    public CheckoutOverviewPage fillAndContinue(String firstName, String lastName, String postalCode) {
        enterFirstName(firstName);
        enterLastName(lastName);
        enterPostalCode(postalCode);
        click(CONTINUE_BUTTON);
        return new CheckoutOverviewPage(driver);
    }

    public CheckoutInfoPage clickContinueExpectingError(String firstName, String lastName, String postalCode) {
        enterFirstName(firstName);
        enterLastName(lastName);
        enterPostalCode(postalCode);
        click(CONTINUE_BUTTON);
        return this;
    }

    public String getErrorMessage() {
        return getText(ERROR_MESSAGE);
    }

    public boolean isErrorDisplayed() {
        return isDisplayed(ERROR_MESSAGE);
    }

    public CartPage cancel() {
        click(CANCEL_BUTTON);
        return new CartPage(driver);
    }
}
