package com.jetbrains.fileindexing.config;

import com.jetbrains.fileindexing.factory.BeansAbstractFactory;
import com.jetbrains.fileindexing.factory.BeansFactoryImpl;
import lombok.Getter;

/**
 * The {@code FactoryContainer} class provides a singleton instance of {@link BeansAbstractFactory}.
 * It uses lazy initialization with double-checked locking to ensure thread safety and
 * efficient resource usage.
 * <p>
 * This class cannot be instantiated and is intended to be used as a container for the factory.
 * </p>
 */
@Getter
public final class FactoryContainer {

    /**
     * The singleton instance of {@link BeansAbstractFactory}.
     */
    private static BeansAbstractFactory beansAbstractFactory;

    /**
     * Private constructor to prevent instantiation.
     */
    private FactoryContainer() {

    }

    /**
     * Returns the singleton instance of {@link BeansAbstractFactory}.
     * If the instance does not exist yet, it is created in a thread-safe manner.
     *
     * @return the singleton instance of {@link BeansAbstractFactory}
     */
    public static BeansAbstractFactory beansAbstractFactory() {
        if (beansAbstractFactory == null) {
            synchronized (FactoryContainer.class) {
                if (beansAbstractFactory == null) {
                    beansAbstractFactory = new BeansFactoryImpl();
                }
            }
        }
        return beansAbstractFactory;
    }
}
