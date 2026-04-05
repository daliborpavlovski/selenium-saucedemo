package com.saucedemo.utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScreenshotUtil {

    private static final ConfigReader config = ConfigReader.getInstance();
    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    private ScreenshotUtil() {}

    /**
     * Captures a screenshot and saves it under test-output/screenshots/.
     *
     * @param driver   active WebDriver instance
     * @param testName name used to identify the screenshot file
     * @return absolute path to the saved file, or empty string on failure
     */
    public static String capture(WebDriver driver, String testName) {
        try {
            String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMATTER);
            String filename = testName.replaceAll("[^a-zA-Z0-9_-]", "_") + "_" + timestamp + ".png";

            Path dir = Paths.get(config.screenshotsDir());
            Files.createDirectories(dir);

            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Path dest = dir.resolve(filename);
            Files.copy(src.toPath(), dest);

            return dest.toAbsolutePath().toString();
        } catch (IOException e) {
            System.err.println("Failed to capture screenshot: " + e.getMessage());
            return "";
        }
    }
}
