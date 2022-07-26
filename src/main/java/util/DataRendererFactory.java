package util;

import org.apache.logging.log4j.Logger;
import services.DataRenderer;

import java.io.IOException;
import java.util.Map;

public class DataRendererFactory {

    private final Logger applicationLogger;
    private final Map<Integer, HeroFactory.HeroNameColumnIndex> heroIdMap;

    public DataRendererFactory(Logger applicationLogger, Map<Integer, HeroFactory.HeroNameColumnIndex> heroIdMap) {
        this.applicationLogger = applicationLogger;
        this.heroIdMap = heroIdMap;
    }

    public DataRenderer dataRenderer() throws IOException {
        return new DataRenderer(
                heroIdMap,
                applicationLogger
        );
    }
}
