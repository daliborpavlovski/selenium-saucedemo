package com.saucedemo.tests;

import com.saucedemo.base.BaseTest;
import com.saucedemo.pages.InventoryPage;
import com.saucedemo.pages.LoginPage;
import com.saucedemo.pages.ProductPage;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


import java.util.List;

import static com.saucedemo.tests.TestData.*;
import static org.testng.Assert.*;

public class InventoryTest extends BaseTest {

    private InventoryPage inventory;

    @BeforeMethod(alwaysRun = true)
    public void loginFirst() {
        inventory = new LoginPage(driver)
                .open()
                .loginAs(config.standardUser(), config.password());
    }

    @Test(description = "Inventory page displays 6 products by default")
    public void inventoryShowsSixProducts() {
        assertEquals(inventory.getProductCount(), 6,
                "Sauce Demo always has exactly 6 products");
    }

    @Test(description = "Products can be sorted alphabetically A to Z")
    public void sortByNameAscending() {
        inventory.sortBy(SORT_NAME_ASC);
        List<String> names = inventory.getProductNames();

        for (int i = 0; i < names.size() - 1; i++) {
            assertTrue(names.get(i).compareToIgnoreCase(names.get(i + 1)) <= 0,
                    "Products should be in ascending alphabetical order");
        }
    }

    @Test(description = "Products can be sorted alphabetically Z to A")
    public void sortByNameDescending() {
        inventory.sortBy(SORT_NAME_DESC);
        List<String> names = inventory.getProductNames();

        for (int i = 0; i < names.size() - 1; i++) {
            assertTrue(names.get(i).compareToIgnoreCase(names.get(i + 1)) >= 0,
                    "Products should be in descending alphabetical order");
        }
    }

    @Test(description = "Products can be sorted by price low to high")
    public void sortByPriceLowToHigh() {
        inventory.sortBy(SORT_PRICE_LOW_HIGH);
        List<Double> prices = inventory.getProductPrices();

        for (int i = 0; i < prices.size() - 1; i++) {
            assertTrue(prices.get(i) <= prices.get(i + 1),
                    "Prices should be in ascending order");
        }
    }

    @Test(description = "Products can be sorted by price high to low")
    public void sortByPriceHighToLow() {
        inventory.sortBy(SORT_PRICE_HIGH_LOW);
        List<Double> prices = inventory.getProductPrices();

        for (int i = 0; i < prices.size() - 1; i++) {
            assertTrue(prices.get(i) >= prices.get(i + 1),
                    "Prices should be in descending order");
        }
    }

    @Test(description = "Cart badge count increases when a product is added")
    public void addingProductUpdatesCartBadge() {
        assertEquals(inventory.getCartBadgeCount(), 0, "Cart should start empty");

        inventory.addToCartByName(BACKPACK);

        assertEquals(inventory.getCartBadgeCount(), 1,
                "Cart badge should show 1 after adding one item");
    }

    @Test(description = "Multiple products can be added; badge reflects the count")
    public void addMultipleProductsUpdatesCartCount() {
        inventory.addToCartByName(BACKPACK);
        inventory.addToCartByName(BIKE_LIGHT);

        assertEquals(inventory.getCartBadgeCount(), 2,
                "Cart badge should show 2 after adding two items");
    }

    @Test(description = "Clicking a product name opens the product detail page")
    public void clickingProductNameOpensDetailPage() {
        ProductPage product = inventory.openProduct(BACKPACK);

        assertEquals(product.getProductName(), BACKPACK,
                "Detail page should show the correct product name");
        assertTrue(product.getPrice() > 0, "Product price should be positive");
    }

    @Test(description = "Product detail page has an Add to Cart button initially")
    public void productDetailPageHasAddToCartButton() {
        ProductPage product = inventory.openProduct(BIKE_LIGHT);

        assertTrue(product.isAddToCartButtonVisible(),
                "Add to Cart button should be present before adding item");
        assertFalse(product.isRemoveButtonVisible(),
                "Remove button should not be visible before adding");
    }

    @Test(description = "Adding from product detail page shows Remove button and updates badge")
    public void addFromDetailPageUpdatesUI() {
        ProductPage product = inventory.openProduct(FLEECE_JACKET);
        product.addToCart();

        assertTrue(product.isRemoveButtonVisible(),
                "Remove button should appear after adding to cart");
        assertEquals(product.getCartBadgeCount(), 1,
                "Cart badge should reflect the added item");
    }

    @Test(description = "Back to Products button returns to inventory")
    public void backToProductsNavigatesToInventory() {
        ProductPage product = inventory.openProduct(BACKPACK);
        InventoryPage back = product.goBackToProducts();

        assertTrue(back.isOnInventoryPage(), "Should be back on the inventory page");
    }

}
