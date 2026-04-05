package com.saucedemo.base;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.saucedemo.utils.ConfigReader;
import com.saucedemo.utils.DriverFactory;
import com.saucedemo.utils.ExtentReportManager;
import com.saucedemo.utils.ScreenshotUtil;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;

/**
 * Base class for all test classes.
 *
 * Lifecycle:
 *   @BeforeSuite  — initialise ExtentReports
 *   @BeforeMethod — create WebDriver + new ExtentTest node
 *   @AfterMethod  — log pass/fail, attach screenshots on failure, quit driver
 *   @AfterSuite   — flush the report to disk
 */
public abstract class BaseTest {

    protected WebDriver driver;
    protected final ConfigReader config = ConfigReader.getInstance();

    @BeforeSuite(alwaysRun = true)
    public void suiteSetup() {
        ExtentReportManager.getInstance();
    }

    @BeforeMethod(alwaysRun = true)
    public void setUp(ITestResult result) {
        driver = DriverFactory.getDriver();
        ExtentTest test = ExtentReportManager.getInstance()
                .createTest(result.getMethod().getMethodName(),
                            result.getMethod().getDescription());
        ExtentReportManager.setTest(test);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        ExtentTest test = ExtentReportManager.getTest();

        if (result.getStatus() == ITestResult.FAILURE) {
            String screenshotPath = ScreenshotUtil.capture(driver, result.getName());
            test.log(Status.FAIL, "Test failed: " + result.getThrowable());
            if (!screenshotPath.isEmpty()) {
                test.addScreenCaptureFromPath(screenshotPath, "Failure screenshot");
            }
        } else if (result.getStatus() == ITestResult.SKIP) {
            test.log(Status.SKIP, "Test skipped: " + result.getThrowable());
        } else {
            test.log(Status.PASS, "Test passed");
        }

        DriverFactory.quitDriver();
    }

    @AfterSuite(alwaysRun = true)
    public void suiteTearDown() {
        ExtentReportManager.flush();
    }
}
