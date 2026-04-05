package com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

public class CheckoutOverviewPage extends BasePage {

    private static final By PAGE_TITLE       = By.cssSelector(".title");
    private static final By ITEM_NAMES       = By.cssSelector(".inventory_item_name");
    private static final By ITEM_PRICES      = By.cssSelector(".inventory_item_price");
    private static final By SUBTOTAL_LABEL   = By.cssSelector(".summary_subtotal_label");
    private static final By TAX_LABEL        = By.cssSelector(".summary_tax_label");
    private static final By TOTAL_LABEL      = By.cssSelector(".summary_total_label");
    private static final By FINISH_BUTTON    = By.id("finish");
    private static final By CANCEL_BUTTON    = By.id("cancel");

    public CheckoutOverviewPage(WebDriver driver) {
        super(driver);
    }

    public boolean isOnCheckoutOverviewPage() {
        return getCurrentUrl().contains("/checkout-step-two.html");
    }

    public String getHeading() {
        return getText(PAGE_TITLE);
    }

    public List<String> getItemNames() {
        return waitForAllVisible(ITEM_NAMES)
                .stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    public double getSubtotal() {
        // Label text: "Item total: $XX.XX"
        return parseAmount(getText(SUBTOTAL_LABEL));
    }

    public double getTax() {
        return parseAmount(getText(TAX_LABEL));
    }

    public double getTotal() {
        return parseAmount(getText(TOTAL_LABEL));
    }

    private double parseAmount(String label) {
        // Extract the number after "$"
        return Double.parseDouble(label.replaceAll("[^0-9.]", ""));
    }

    public CheckoutCompletePage finish() {
        click(FINISH_BUTTON);
        return new CheckoutCompletePage(driver);
    }

    public InventoryPage cancel() {
        click(CANCEL_BUTTON);
        return new InventoryPage(driver);
    }
}
