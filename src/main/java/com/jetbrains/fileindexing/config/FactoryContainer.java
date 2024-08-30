package com.jetbrains.fileindexing.config;

import com.jetbrains.fileindexing.factory.FileSystemAbstractFactory;
import com.jetbrains.fileindexing.factory.FileSystemAbstractFactoryImpl;
import lombok.Getter;

@Getter
public final class FactoryContainer {

    private static FileSystemAbstractFactory instance;

    private FactoryContainer() {

    }

    // Get the instance
    public static FileSystemAbstractFactory instance() {
        if (instance == null) {
            synchronized (FactoryContainer.class) {
                if (instance == null) {
                    instance = new FileSystemAbstractFactoryImpl();
                }
            }
        }
        return instance;
    }

}
