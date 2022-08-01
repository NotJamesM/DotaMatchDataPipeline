package com.github.notjamesm.util;

import com.github.notjamesm.services.Valve;
import com.github.notjamesm.settings.ValveSettings;
import com.google.common.util.concurrent.RateLimiter;
import org.slf4j.Logger;

import java.net.http.HttpClient;

import static com.github.notjamesm.util.ObjectMapperFactory.createObjectMapper;

public class ValveFactory {
    private final ValveSettings valveSettings;
    private final Logger logger;
    private final HttpClient httpClient;

    public ValveFactory(HttpClient httpClient, ValveSettings valveSettings, Logger logger) {
        this.httpClient = httpClient;
        this.valveSettings = valveSettings;
        this.logger = logger;
    }

    public Valve valve() {
        return new Valve(valveSettings,
                new RateLimitedHttpClient(httpClient, RateLimiter.create(valveSettings.rateLimit()), logger),
                createObjectMapper(),
                logger);
    }
}
