package com.jetbrains.fileindexing.config;

import com.jetbrains.fileindexing.factory.BeansAbstractFactory;
import com.jetbrains.fileindexing.factory.BeansAbstractFactoryImpl;
import lombok.Getter;

@Getter
public final class FactoryContainer {

    private static BeansAbstractFactory beansAbstractFactory;

    private FactoryContainer() {

    }

    // Get the instance
    public static BeansAbstractFactory beansAbstractFactory() {
        if (beansAbstractFactory == null) {
            synchronized (FactoryContainer.class) {
                if (beansAbstractFactory == null) {
                    beansAbstractFactory = new BeansAbstractFactoryImpl();
                }
            }
        }
        return beansAbstractFactory;
    }

}
