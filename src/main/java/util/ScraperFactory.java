package util;

import org.slf4j.Logger;
import services.Valve;

public class ScraperFactory {

    private final Valve valve;
    private final SequenceNumberRepository sequenceNumberRepository;
    private final Logger logger;

    public ScraperFactory(Valve valve, SequenceNumberRepository sequenceNumberRepository, Logger logger) {
        this.valve = valve;
        this.sequenceNumberRepository = sequenceNumberRepository;
        this.logger = logger;
    }

    public Scraper scraper() {
        return new Scraper(valve, sequenceNumberRepository, logger);
    }
}
