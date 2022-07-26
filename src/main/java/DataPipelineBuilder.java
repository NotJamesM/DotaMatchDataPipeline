import Settings.Settings;
import org.slf4j.Logger;
import services.DataRenderer;
import services.Valve;
import util.*;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

public class DataPipelineBuilder {

    private final Settings settings;
    private final Logger applicationLogger;

    public DataPipelineBuilder(Settings settings, Logger applicationLogger) {
        this.settings = settings;
        this.applicationLogger = applicationLogger;
    }

    public DataPipeline build() throws IOException {
        final Valve valve = new ValveFactory(settings, applicationLogger).valve();
        final Scraper scraper = new ScraperFactory(valve, applicationLogger).scraper();

        final DataFixer dataFixer = new DataFixer(applicationLogger);
        Map<Integer, HeroFactory.HeroNameColumnIndex> heroIdMap = new HeroFactory().initialiseHeroes(Path.of("heroes.json")); //TODO: make a property
        final DataRenderer dataRenderer = new DataRendererFactory(applicationLogger, heroIdMap).dataRenderer();
        return new DataPipeline(scraper, dataFixer, dataRenderer, applicationLogger);
    }
}
