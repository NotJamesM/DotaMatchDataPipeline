package com.github.notjamesm.settings;

import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class Settings implements ValveSettings, SequenceNumberRepositorySettings, HeroFactorySettings, DataRenderSettings {

    private final Properties properties;
    private final Logger applicationLogger;

    public Settings(String path, Logger applicationLogger) {
        this.applicationLogger = applicationLogger;
        applicationLogger.info("Attempting to load properties from {}", path);
        properties = new Properties();
        try {
            properties.load(Files.newBufferedReader(Path.of(path)));
        } catch (IOException e) {
            applicationLogger.error("Failed to load properties from {}", path);
            throw new RuntimeException(e);
        }
    }

    @Override
    public String valveApiKey() {
        return properties.getProperty("valve.api.key");
    }

    @Override
    public double rateLimit() {
        return Double.parseDouble(properties.getProperty("rate.limit"));
    }

    @Override
    public String baseValveApiUrl() {
        return properties.getProperty("base.valve.api.url");
    }

    @Override
    public String getMatchHistoryPath() {
        return properties.getProperty("get.match.history.path");
    }

    @Override
    public String getMatchHistoryBySeqNumPath() {
        return properties.getProperty("get.match.history.by.seq.num.path");
    }

    @Override
    public String getMatchDetailsPath() {
        return properties.getProperty("get.match.details.path");
    }

    @Override
    public Path getSequenceNumberFilePath() {
        return Path.of(properties.getProperty("sequence.number.file.path"));
    }

    @Override
    public Path getHeroesJsonFilePath() {
        return Path.of(properties.getProperty("heroes.json.file.path"));
    }

    @Override
    public Path getExportDirectoryPath() {
        return Path.of(properties.getProperty("export.directory.path"));
    }
}
