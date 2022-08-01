package com.github.notjamesm.settings;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class Settings implements ValveSettings, SequenceNumberRepositorySettings, HeroFactorySettings, DataRenderSettings, DataPipelineSettings {

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
        return getOrThrowRuntimeException("valve.api.key");
    }

    @Override
    public double rateLimit() {
        return Double.parseDouble(getOrThrowRuntimeException("requests.to.valve.per.second"));
    }

    @Override
    public String baseValveApiUrl() {
        return getOrThrowRuntimeException("base.valve.api.url");
    }

    @Override
    public String getMatchHistoryPath() {
        return getOrThrowRuntimeException("get.match.history.path");
    }

    @Override
    public String getMatchHistoryBySeqNumPath() {
        return getOrThrowRuntimeException("get.match.history.by.seq.num.path");
    }

    @Override
    public String getMatchDetailsPath() {
        return getOrThrowRuntimeException("get.match.details.path");
    }

    @Override
    public Path getSequenceNumberFilePath() {
        return Path.of(getOrThrowRuntimeException("sequence.number.file.path"));
    }

    @Override
    public Path getHeroesJsonFilePath() {
        return Path.of(getOrThrowRuntimeException("heroes.json.file.path"));
    }

    @Override
    public Path getExportDirectoryPath() {
        return Path.of(getOrThrowRuntimeException("export.directory.path"));
    }

    @Override
    public int numberOfMatchesToScrapeFor() {
        return Integer.parseInt(getOrThrowRuntimeException("number.of.matches.to.scrape.for"));
    }

    private String getOrThrowRuntimeException(String key) {
        final String propertyValue = properties.getProperty(key);
        if (propertyValue == null || StringUtils.isBlank(propertyValue)) {
            applicationLogger.error("Property '{}' is missing or not set correctly.", key);
            throw new RuntimeException("Property '%s' is missing or not set correctly.".formatted(key));
        }
        return propertyValue;
    }
}
