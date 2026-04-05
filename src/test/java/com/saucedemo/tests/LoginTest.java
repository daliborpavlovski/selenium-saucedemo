package com.saucedemo.tests;

import com.saucedemo.base.BaseTest;
import com.saucedemo.pages.InventoryPage;
import com.saucedemo.pages.LoginPage;
import org.testng.annotations.Test;


import static org.testng.Assert.*;

public class LoginTest extends BaseTest {

    // -----------------------------------------------------------------------
    // Positive scenarios
    // -----------------------------------------------------------------------

    @Test(description = "Standard user can log in and land on the inventory page")
    public void standardUserCanLogin() {
        LoginPage login = new LoginPage(driver).open();

        InventoryPage inventory = login.loginAs(
                config.standardUser(), config.password());

        assertTrue(inventory.isOnInventoryPage(),
                "Expected inventory URL after successful login");
        assertEquals(inventory.getHeading(), "Products",
                "Expected 'Products' page heading");
    }

    @Test(description = "Performance-glitch user can eventually log in")
    public void performanceGlitchUserCanLogin() {
        LoginPage login = new LoginPage(driver).open();

        InventoryPage inventory = login.loginAs(
                config.perfGlitchUser(), config.password());

        assertTrue(inventory.isOnInventoryPage(),
                "Performance glitch user should still reach inventory (just slower)");
    }

    // -----------------------------------------------------------------------
    // Negative scenarios
    // -----------------------------------------------------------------------

    @Test(description = "Locked-out user sees an error message and stays on login page")
    public void lockedOutUserSeesError() {
        LoginPage login = new LoginPage(driver).open();

        login.loginExpectingError(config.lockedUser(), config.password());

        assertTrue(login.isErrorDisplayed(), "Error message should be visible");
        assertTrue(login.getErrorMessage().contains("locked out"),
                "Error should mention 'locked out'");
    }

    @Test(description = "Wrong password shows an error")
    public void wrongPasswordShowsError() {
        LoginPage login = new LoginPage(driver).open();

        login.loginExpectingError(config.standardUser(), "wrong_password");

        assertTrue(login.isErrorDisplayed(), "Error message should be visible");
        assertTrue(login.getErrorMessage().contains("Username and password do not match"),
                "Error should mention credential mismatch");
    }

    @Test(description = "Empty username shows validation error")
    public void emptyUsernameShowsError() {
        LoginPage login = new LoginPage(driver).open();

        login.loginExpectingError("", config.password());

        assertTrue(login.isErrorDisplayed(), "Validation error should be visible");
        assertTrue(login.getErrorMessage().contains("Username is required"),
                "Error should ask for username");
    }

    @Test(description = "Empty password shows validation error")
    public void emptyPasswordShowsError() {
        LoginPage login = new LoginPage(driver).open();

        login.loginExpectingError(config.standardUser(), "");

        assertTrue(login.isErrorDisplayed(), "Validation error should be visible");
        assertTrue(login.getErrorMessage().contains("Password is required"),
                "Error should ask for password");
    }

    @Test(description = "Error banner can be dismissed with the X button")
    public void errorBannerCanBeDismissed() {
        LoginPage login = new LoginPage(driver).open();
        login.loginExpectingError("", "");

        assertTrue(login.isErrorDisplayed(), "Error should appear first");
        login.closeError();
        assertFalse(login.isErrorDisplayed(), "Error should disappear after close");
    }

    // -----------------------------------------------------------------------
    // Logout
    // -----------------------------------------------------------------------

    @Test(description = "Logged-in user can log out and return to login page")
    public void userCanLogout() {
        LoginPage login = new LoginPage(driver).open();
        InventoryPage inventory = login.loginAs(config.standardUser(), config.password());

        LoginPage afterLogout = inventory.logout();

        assertTrue(afterLogout.isOnLoginPage(), "User should be back on the login page");
    }

}
