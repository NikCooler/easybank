package org.craftedsw.config;

import java.io.InputStream;
import java.util.Properties;

/**
 * App configuration
 *
 * @author Nikolay Smirnov
 */
public class AppConfiguration {

    private static final String PROPERTY_FILE_NAME = "app.properties";
    private final Properties properties = new Properties();

    private AppConfiguration() {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(PROPERTY_FILE_NAME)) {
            this.properties.load(is);
        } catch (Exception e) {
            // NOPE
        }
    }

    public Integer getIntProperty(String key) {
        return Integer.valueOf(properties.getProperty(key));
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public static AppConfiguration getInstance() {
        return LazyConfigurationHolder.APP_CONFIGURATION;
    }

    private static class LazyConfigurationHolder {
        private static final AppConfiguration APP_CONFIGURATION = new AppConfiguration();
    }

}
