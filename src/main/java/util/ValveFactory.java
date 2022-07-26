package util;

import Settings.ValveSettings;
import org.apache.logging.log4j.Logger;
import services.Valve;

import static util.ObjectMapperFactory.createObjectMapper;

public class ValveFactory {
    private final ValveSettings valveSettings;
    private final Logger logger;

    public ValveFactory(ValveSettings valveSettings, Logger logger) {
        this.valveSettings = valveSettings;
        this.logger = logger;
    }

    public Valve valve() {
        return new Valve(valveSettings, RateLimitedHttpClient.rateLimitedHttpClient(1), createObjectMapper(), logger);
    }
}
