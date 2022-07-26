package util;

import org.apache.logging.log4j.Logger;
import services.Valve;

public class ScraperFactory {

    private final Valve valve;
    private final Logger logger;

    public ScraperFactory(Valve valve, Logger logger) {
        this.valve = valve;
        this.logger = logger;
    }

    public Scraper scraper() {
        return new Scraper(valve, logger);
    }
}
