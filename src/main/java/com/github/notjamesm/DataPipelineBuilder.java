package com.github.notjamesm;

import com.github.notjamesm.services.Valve;
import com.github.notjamesm.settings.Settings;
import com.github.notjamesm.util.*;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.http.HttpClient;
import java.util.Map;

public class DataPipelineBuilder {

    private final Settings settings;
    private final Logger applicationLogger;

    public DataPipelineBuilder(Settings settings, Logger applicationLogger) {
        this.settings = settings;
        this.applicationLogger = applicationLogger;
    }

    public DataPipeline build() throws IOException {
        final HttpClient httpClient = HttpClient.newBuilder().build();

        final Valve valve = new ValveFactory(httpClient, settings, applicationLogger).valve();
        final Scraper scraper = new ScraperFactory(valve, new SequenceNumberRepository(settings, applicationLogger), applicationLogger).scraper();

        final DataFixer dataFixer = new DataFixer(applicationLogger);
        Map<Integer, HeroFactory.HeroNameColumnIndex> heroIdMap = new HeroFactory().initialiseHeroes(settings.getHeroesJsonFilePath());
        final DataRenderer dataRenderer = new DataRendererFactory(applicationLogger, settings, heroIdMap).dataRenderer();
        return new DataPipeline(scraper, dataFixer, dataRenderer, settings, applicationLogger);
    }
}
