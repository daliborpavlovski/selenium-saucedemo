package com.saucedemo.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;


/**
 * Creates and manages a thread-local WebDriver instance.
 * Thread-local storage allows safe parallel test execution.
 */
public class DriverFactory {

    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();
    private static final ConfigReader config = ConfigReader.getInstance();

    private DriverFactory() {}

    public static WebDriver getDriver() {
        if (driverThreadLocal.get() == null) {
            driverThreadLocal.set(createDriver(config.browser()));
        }
        return driverThreadLocal.get();
    }

    private static WebDriver createDriver(String browser) {
        boolean headless = config.headless();
        WebDriver driver;

        switch (browser.toLowerCase()) {
            case "firefox" -> {
                FirefoxOptions opts = new FirefoxOptions();
                if (headless) opts.addArguments("--headless");
                driver = new FirefoxDriver(opts);
            }
            case "edge" -> {
                EdgeOptions opts = new EdgeOptions();
                if (headless) opts.addArguments("--headless");
                driver = new EdgeDriver(opts);
            }
            default -> {
                // Selenium Manager (built into Selenium 4.6+) resolves ChromeDriver automatically.
                ChromeOptions opts = new ChromeOptions();
                if (headless) {
                    opts.addArguments("--headless=new");
                    opts.addArguments("--window-size=1920,1080");
                }
                opts.addArguments("--disable-notifications");
                opts.addArguments("--no-sandbox");
                opts.addArguments("--disable-dev-shm-usage");
                driver = new ChromeDriver(opts);
            }
        }

        driver.manage().window().maximize();
        return driver;
    }

    public static void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            driver.quit();
            driverThreadLocal.remove();
        }
    }
}
