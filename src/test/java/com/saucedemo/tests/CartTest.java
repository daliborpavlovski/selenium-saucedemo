package com.saucedemo.tests;

import com.saucedemo.base.BaseTest;
import com.saucedemo.pages.CartPage;
import com.saucedemo.pages.InventoryPage;
import com.saucedemo.pages.LoginPage;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.saucedemo.tests.TestData.*;
import static org.testng.Assert.*;

public class CartTest extends BaseTest {

    private InventoryPage inventory;

    @BeforeMethod(alwaysRun = true)
    public void loginFirst() {
        inventory = new LoginPage(driver)
                .open()
                .loginAs(config.standardUser(), config.password());
    }

    @Test(description = "Cart is empty after login — no badge visible")
    public void cartIsEmptyAfterLogin() {
        CartPage cart = inventory.goToCart();

        assertTrue(cart.isOnCartPage(), "Should be on cart page");
        assertTrue(cart.isCartEmpty(), "Cart should be empty after login");
    }

    @Test(description = "Added product appears in cart with correct name")
    public void addedProductAppearsInCart() {
        inventory.addToCartByName(BACKPACK);
        CartPage cart = inventory.goToCart();

        assertEquals(cart.getItemCount(), 1, "Cart should contain one item");
        assertTrue(cart.getItemNames().contains(BACKPACK),
                "Cart should contain the added product");
    }

    @Test(description = "Multiple added products all appear in the cart")
    public void multipleProductsInCart() {
        inventory.addToCartByName(BACKPACK);
        inventory.addToCartByName(BIKE_LIGHT);
        inventory.addToCartByName(BOLT_T_SHIRT);
        CartPage cart = inventory.goToCart();

        assertEquals(cart.getItemCount(), 3, "Cart should contain three items");
    }

    @Test(description = "Product can be removed from the cart")
    public void productCanBeRemovedFromCart() {
        inventory.addToCartByName(BACKPACK);
        CartPage cart = inventory.goToCart();
        assertEquals(cart.getItemCount(), 1, "Cart should contain one item before removal");

        cart.removeItemByName(BACKPACK);

        assertTrue(cart.isCartEmpty(), "Cart should be empty after removal");
    }

    @Test(description = "All products can be removed from the cart at once")
    public void allProductsCanBeRemovedFromCart() {
        inventory.addToCartByName(BACKPACK);
        inventory.addToCartByName(BIKE_LIGHT);
        CartPage cart = inventory.goToCart();

        cart.removeAllItems();

        assertTrue(cart.isCartEmpty(), "Cart should be empty after removing all items");
    }

    @Test(description = "Cart item prices match the prices shown on the inventory page")
    public void cartPricesMatchInventoryPrices() {
        double inventoryPrice = inventory.getProductPrices().get(
                inventory.getProductNames().indexOf(BACKPACK));

        inventory.addToCartByName(BACKPACK);
        CartPage cart = inventory.goToCart();

        assertEquals(cart.getItemPrices().get(0), inventoryPrice,
                "Price in cart should match price on inventory page");
    }

    @Test(description = "Continue Shopping button returns user to inventory")
    public void continueShoppingNavigatesToInventory() {
        CartPage cart = inventory.goToCart();
        InventoryPage back = cart.continueShopping();

        assertTrue(back.isOnInventoryPage(),
                "Continue Shopping should take user back to inventory");
    }

    @Test(description = "Cart badge disappears when the last item is removed")
    public void cartBadgeDisappearsWhenCartCleared() {
        inventory.addToCartByName(BACKPACK);
        assertEquals(inventory.getCartBadgeCount(), 1,
                "Cart badge should show 1 after adding one item");

        CartPage cart = inventory.goToCart();
        cart.removeAllItems();
        InventoryPage back = cart.continueShopping();

        assertEquals(back.getCartBadgeCount(), 0,
                "Cart badge should be gone when cart is empty");
    }

}
