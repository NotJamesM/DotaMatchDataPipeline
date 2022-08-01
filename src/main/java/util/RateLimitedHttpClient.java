package util;

import com.google.common.util.concurrent.RateLimiter;
import org.slf4j.Logger;
import services.ValveException;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static java.lang.String.format;

@SuppressWarnings("UnstableApiUsage")
public class RateLimitedHttpClient {

    private final HttpClient httpClient;
    private final RateLimiter rateLimiter;
    private final Logger applicationLogger;

    public RateLimitedHttpClient(HttpClient httpClient, RateLimiter rateLimiter, Logger applicationLogger) {
        this.httpClient = httpClient;
        this.rateLimiter = rateLimiter;
        this.applicationLogger = applicationLogger;
    }

    public HttpResponse<String> doRequest(HttpRequest httpRequest) throws IOException, InterruptedException {
        rateLimiter.acquire(1);
        applicationLogger.info("Request to Valve:\n{}", httpRequest);
        final HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        applicationLogger.info("Response from Valve:\n{}", httpResponse);
        if (httpResponse.statusCode() != 200) {
            throw new ValveException(format("Response from steam API failed: %s\n%s", httpResponse.statusCode(), httpResponse.body()));
        }
        return httpResponse;
    }
}
