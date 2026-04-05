package com.saucedemo.tests;

/**
 * Centralised test data constants for Sauce Demo.
 * Product names and sort options are derived from the AUT's seeded catalogue.
 */
public final class TestData {

    private TestData() {}

    // -----------------------------------------------------------------------
    // Products
    // -----------------------------------------------------------------------

    public static final String BACKPACK       = "Sauce Labs Backpack";
    public static final String BIKE_LIGHT     = "Sauce Labs Bike Light";
    public static final String BOLT_T_SHIRT   = "Sauce Labs Bolt T-Shirt";
    public static final String FLEECE_JACKET  = "Sauce Labs Fleece Jacket";
    public static final String ONESIE         = "Sauce Labs Onesie";
    public static final String RED_T_SHIRT    = "Test.allTheThings() T-Shirt (Red)";

    // -----------------------------------------------------------------------
    // Sort options (visible dropdown text)
    // -----------------------------------------------------------------------

    public static final String SORT_NAME_ASC        = "Name (A to Z)";
    public static final String SORT_NAME_DESC       = "Name (Z to A)";
    public static final String SORT_PRICE_LOW_HIGH  = "Price (low to high)";
    public static final String SORT_PRICE_HIGH_LOW  = "Price (high to low)";
}
