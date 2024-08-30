package com.jetbrains.fileindexing.config;

import com.jetbrains.fileindexing.search.SearchStrategy;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.io.File;

@Getter
@ToString
@Builder(builderMethodName = "internalBuilder")
public final class Config {
    private final File watchingFolder;
    private final SearchStrategy searchStrategy;

    // The single instance of the Config class
    private static Config instance;

    // Private constructor to prevent instantiation
    private Config(File watchingFolder, SearchStrategy searchStrategy) {
        assert watchingFolder != null;
        assert searchStrategy != null;
        this.watchingFolder = watchingFolder;
        this.searchStrategy = searchStrategy;
    }

    // Public static method to get the instance
    public static Config getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Config has not been initialized. Call Config.builder().build() first.");
        }
        return instance;
    }

    // Public builder method
    public static ConfigBuilder builder() {
        if (instance == null) {
            return internalBuilder();
        }
        throw new IllegalStateException("Config has already been initialized.");
    }

    // Public build method in the builder class
    public static class ConfigBuilder {
        public Config build() {
            if (Config.instance == null) {
                Config.instance = new Config(watchingFolder, searchStrategy);
            }
            return Config.instance;
        }
    }
}
