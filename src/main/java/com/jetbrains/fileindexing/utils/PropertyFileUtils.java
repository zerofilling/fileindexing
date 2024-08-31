package com.jetbrains.fileindexing.utils;

import lombok.SneakyThrows;

import java.io.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.Properties;

/**
 * Utility class for managing properties in a property file. Provides methods to read and write properties,
 * and ensures thread-safe access to property files with lazy loading.
 */
public class PropertyFileUtils {

    // Thread-safe map to cache properties
    private static final ConcurrentMap<String, Properties> propertiesCache = new ConcurrentHashMap<>();

    /**
     * Puts a key-value pair into the specified property file. If the property file does not exist, it will be created.
     *
     * @param key          The key to be added or updated.
     * @param value        The value associated with the key.
     * @param propertyFile The property file to write to.
     * @throws IOException If an I/O error occurs.
     */
    @SneakyThrows
    public static void put(String key, Object value, File propertyFile) {
        Properties properties = getProperties(propertyFile);

        // Add or update the property
        properties.setProperty(key, value.toString());

        // Save the properties back to the file
        try (FileOutputStream fos = new FileOutputStream(propertyFile)) {
            properties.store(fos, null);
        }
    }

    /**
     * Gets the value associated with the specified key from the property file. If the key is not found, returns the default value.
     *
     * @param key          The key whose value is to be retrieved.
     * @param defaultValue The default value to return if the key is not found.
     * @param propertyFile The property file to read from.
     * @param <T>          The type of the value.
     * @return The value associated with the key, or the default value if the key is not found.
     * @throws IOException If an I/O error occurs.
     */
    @SneakyThrows
    public static <T> T get(String key, T defaultValue, File propertyFile) {
        Properties properties = getProperties(propertyFile);

        // Get the value associated with the key
        String value = properties.getProperty(key);

        // Return the value if found, otherwise return the default value
        if (value != null) {
            return (T) value; // Cast to the generic type
        } else {
            return defaultValue;
        }
    }

    /**
     * Returns the Properties object for the specified property file, loading it lazily.
     *
     * @param propertyFile The property file to get the Properties object for.
     * @return The Properties object.
     * @throws IOException If an I/O error occurs.
     */
    private static Properties getProperties(File propertyFile) throws IOException {
        String filePath = propertyFile.getAbsolutePath();
        Properties properties = propertiesCache.get(filePath);

        if (properties == null) {
            // Synchronize on the filePath to ensure only one thread loads the properties
            synchronized (propertyFile) {
                properties = propertiesCache.get(filePath);
                if (properties == null) {
                    properties = new Properties();
                    if (propertyFile.exists()) {
                        try (FileInputStream fis = new FileInputStream(propertyFile)) {
                            properties.load(fis);
                        }
                    }
                    propertiesCache.put(filePath, properties);
                }
            }
        }
        return properties;
    }
}
