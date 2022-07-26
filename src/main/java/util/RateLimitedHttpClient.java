package util;

import com.google.common.util.concurrent.RateLimiter;
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

    private RateLimitedHttpClient(HttpClient httpClient, RateLimiter rateLimiter) {
        this.httpClient = httpClient;
        this.rateLimiter = rateLimiter;
    }

    public static RateLimitedHttpClient rateLimitedHttpClient(double limit) {
        return new RateLimitedHttpClient(HttpClient.newBuilder().build(), RateLimiter.create(limit));
    }

    public HttpResponse<String> doRequest(HttpRequest httpRequest) throws IOException, InterruptedException {
        rateLimiter.acquire(1);
        final HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        if (httpResponse.statusCode() != 200) {
            throw new ValveException(format("Response from steam API failed: %s\n%s", httpResponse.statusCode(), httpResponse.body()));
        }
        return httpResponse;
    }
}
