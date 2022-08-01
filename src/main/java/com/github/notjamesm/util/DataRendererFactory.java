package com.github.notjamesm.util;

import com.github.notjamesm.settings.DataRenderSettings;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.Map;

public class DataRendererFactory {

    private final Logger applicationLogger;
    private final DataRenderSettings dataRenderSettings;
    private final Map<Integer, HeroFactory.HeroNameColumnIndex> heroIdMap;

    public DataRendererFactory(Logger applicationLogger, DataRenderSettings dataRenderSettings, Map<Integer, HeroFactory.HeroNameColumnIndex> heroIdMap) {
        this.applicationLogger = applicationLogger;
        this.dataRenderSettings = dataRenderSettings;
        this.heroIdMap = heroIdMap;
    }

    public DataRenderer dataRenderer() throws IOException {
        return new DataRenderer(
                heroIdMap,
                dataRenderSettings,
                applicationLogger
        );
    }
}
