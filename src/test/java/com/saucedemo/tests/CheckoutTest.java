package com.saucedemo.tests;

import com.saucedemo.base.BaseTest;
import com.saucedemo.pages.CartPage;
import com.saucedemo.pages.CheckoutCompletePage;
import com.saucedemo.pages.CheckoutInfoPage;
import com.saucedemo.pages.CheckoutOverviewPage;
import com.saucedemo.pages.InventoryPage;
import com.saucedemo.pages.LoginPage;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.saucedemo.tests.TestData.*;
import static org.testng.Assert.*;

public class CheckoutTest extends BaseTest {

    private InventoryPage inventory;

    @BeforeMethod(alwaysRun = true)
    public void loginAndAddItem() {
        inventory = new LoginPage(driver)
                .open()
                .loginAs(config.standardUser(), config.password());
        inventory.addToCartByName(BACKPACK);
    }

    // -----------------------------------------------------------------------
    // Checkout info page
    // -----------------------------------------------------------------------

    @Test(description = "Checkout button on cart leads to checkout info page")
    public void checkoutButtonOpensInfoPage() {
        CheckoutInfoPage infoPage = inventory.goToCart().proceedToCheckout();

        assertTrue(infoPage.isOnCheckoutInfoPage(),
                "Should be on the checkout info step");
    }

    @Test(description = "Missing first name shows a validation error")
    public void missingFirstNameShowsError() {
        CheckoutInfoPage infoPage = inventory.goToCart().proceedToCheckout();
        infoPage.clickContinueExpectingError("", "Doe", "12345");

        assertTrue(infoPage.isErrorDisplayed(), "Error should be visible");
        assertTrue(infoPage.getErrorMessage().contains("First Name is required"),
                "Error should mention first name");
    }

    @Test(description = "Missing last name shows a validation error")
    public void missingLastNameShowsError() {
        CheckoutInfoPage infoPage = inventory.goToCart().proceedToCheckout();
        infoPage.clickContinueExpectingError("John", "", "12345");

        assertTrue(infoPage.isErrorDisplayed(), "Error should be visible");
        assertTrue(infoPage.getErrorMessage().contains("Last Name is required"),
                "Error should mention last name");
    }

    @Test(description = "Missing postal code shows a validation error")
    public void missingPostalCodeShowsError() {
        CheckoutInfoPage infoPage = inventory.goToCart().proceedToCheckout();
        infoPage.clickContinueExpectingError("John", "Doe", "");

        assertTrue(infoPage.isErrorDisplayed(), "Error should be visible");
        assertTrue(infoPage.getErrorMessage().contains("Postal Code is required"),
                "Error should mention postal code");
    }

    @Test(description = "Cancel on info page returns to the cart")
    public void cancelOnInfoPageReturnsToCart() {
        CartPage cart = inventory.goToCart().proceedToCheckout().cancel();

        assertTrue(cart.isOnCartPage(), "Cancel should return to cart");
    }

    // -----------------------------------------------------------------------
    // Checkout overview page
    // -----------------------------------------------------------------------

    @Test(description = "Overview page displays the correct item")
    public void overviewPageShowsCorrectItem() {
        CheckoutOverviewPage overview = inventory.goToCart()
                .proceedToCheckout()
                .fillAndContinue("John", "Doe", "12345");

        assertTrue(overview.isOnCheckoutOverviewPage(), "Should be on overview page");
        assertTrue(overview.getItemNames().contains(BACKPACK),
                "Overview should list the Sauce Labs Backpack");
    }

    @Test(description = "Overview page total equals subtotal plus tax")
    public void overviewTotalEqualsSubtotalPlusTax() {
        CheckoutOverviewPage overview = inventory.goToCart()
                .proceedToCheckout()
                .fillAndContinue("John", "Doe", "12345");

        double expected = Math.round((overview.getSubtotal() + overview.getTax()) * 100.0) / 100.0;
        double actual   = Math.round(overview.getTotal() * 100.0) / 100.0;

        assertEquals(actual, expected,
                "Total should equal subtotal + tax (rounded to 2 decimal places)");
    }

    @Test(description = "Cancel on overview returns to inventory")
    public void cancelOnOverviewReturnsToInventory() {
        InventoryPage back = inventory.goToCart()
                .proceedToCheckout()
                .fillAndContinue("John", "Doe", "12345")
                .cancel();

        assertTrue(back.isOnInventoryPage(), "Cancel on overview should go back to inventory");
    }

    // -----------------------------------------------------------------------
    // Order completion
    // -----------------------------------------------------------------------

    @Test(description = "Completing checkout shows the order confirmation page")
    public void completingCheckoutShowsConfirmation() {
        CheckoutCompletePage complete = inventory.goToCart()
                .proceedToCheckout()
                .fillAndContinue("John", "Doe", "12345")
                .finish();

        assertTrue(complete.isOnCheckoutCompletePage(), "Should land on the complete page");
        assertEquals(complete.getConfirmationHeader(), "Thank you for your order!",
                "Confirmation header text mismatch");
    }

    @Test(description = "Order confirmation page shows the dispatch image")
    public void confirmationPageShowsImage() {
        CheckoutCompletePage complete = inventory.goToCart()
                .proceedToCheckout()
                .fillAndContinue("John", "Doe", "12345")
                .finish();

        assertTrue(complete.isOrderConfirmationImageVisible(),
                "Confirmation image (pony express) should be visible");
    }

    @Test(description = "Back to Home on confirmation returns to inventory")
    public void backHomeReturnsToInventory() {
        InventoryPage back = inventory.goToCart()
                .proceedToCheckout()
                .fillAndContinue("John", "Doe", "12345")
                .finish()
                .backToHome();

        assertTrue(back.isOnInventoryPage(), "Back to Home should go to inventory");
    }

    @Test(description = "End-to-end: full purchase flow with multiple products")
    public void fullPurchaseFlow() {
        // Add a second item (BACKPACK was added in @BeforeMethod)
        inventory.addToCartByName(BIKE_LIGHT);
        assertEquals(inventory.getCartBadgeCount(), 2,
                "Cart badge should show 2 after adding two items");

        // Cart → checkout
        CartPage cart = inventory.goToCart();
        assertEquals(cart.getItemCount(), 2, "Cart should have 2 items");

        CheckoutCompletePage complete = cart.proceedToCheckout()
                .fillAndContinue("Jane", "Smith", "90210")
                .finish();

        assertTrue(complete.isOnCheckoutCompletePage(), "Should reach order confirmation");
        assertEquals(complete.getConfirmationHeader(), "Thank you for your order!",
                "Confirmation header text mismatch");
    }

}
