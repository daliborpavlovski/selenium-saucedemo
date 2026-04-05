package com.saucedemo.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Reads configuration values from config.properties.
 * Uses the singleton pattern so properties are loaded once per run.
 */
public class ConfigReader {

    private static final String CONFIG_FILE = "config.properties";
    private static ConfigReader instance;
    private final Properties properties;

    private ConfigReader() {
        properties = new Properties();
        try (InputStream stream = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (stream == null) {
                throw new RuntimeException("config.properties not found on the classpath");
            }
            properties.load(stream);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load " + CONFIG_FILE, e);
        }
    }

    public static synchronized ConfigReader getInstance() {
        if (instance == null) {
            instance = new ConfigReader();
        }
        return instance;
    }

    public String get(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            throw new RuntimeException("Missing required config key: " + key);
        }
        return value.trim();
    }

    public String get(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue).trim();
    }

    public int getInt(String key) {
        return Integer.parseInt(get(key));
    }

    public boolean getBoolean(String key) {
        return Boolean.parseBoolean(get(key));
    }

    // Convenience accessors
    public String baseUrl()         { return get("base.url"); }
    public String browser()         { return get("browser"); }
    public boolean headless()       { return getBoolean("headless"); }
    public int implicitWait()       { return getInt("implicit.wait"); }
    public int explicitWait()       { return getInt("explicit.wait"); }
    public String reportsDir()      { return get("reports.dir"); }
    public String screenshotsDir()  { return get("screenshots.dir"); }

    public String standardUser()    { return get("user.standard"); }
    public String lockedUser()      { return get("user.locked"); }
    public String problemUser()     { return get("user.problem"); }
    public String perfGlitchUser()  { return get("user.performance"); }
    public String errorUser()       { return get("user.error"); }
    public String visualUser()      { return get("user.visual"); }
    public String password()        { return get("password"); }
}
