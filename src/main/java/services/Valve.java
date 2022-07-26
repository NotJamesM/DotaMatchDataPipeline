package services;

import Settings.ValveSettings;
import com.fasterxml.jackson.databind.ObjectMapper;
import domain.valve.MatchDetailsResult;
import domain.valve.MatchHistoryResult;
import org.apache.logging.log4j.Logger;
import util.RateLimitedHttpClient;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static java.lang.String.format;

public class Valve {

    private final ValveSettings valveSettings;
    private final RateLimitedHttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final Logger applicationLogger;

    public Valve(ValveSettings valveSettings,
                 RateLimitedHttpClient httpClient,
                 ObjectMapper objectMapper,
                 Logger applicationLogger) {
        this.valveSettings = valveSettings;
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
        this.applicationLogger = applicationLogger;
    }

    public MatchHistoryResult getMatches(int matchesToRequest) {
        HttpRequest httpRequest = createGetMatchesRequest(matchesToRequest);
        try {
            HttpResponse<String> response = httpClient.doRequest(httpRequest);
            final MatchHistoryResult matchHistoryResult = objectMapper.readValue(response.body(), MatchHistoryResult.class);
            applicationLogger.info("Got match history, number of matches: {}", matchHistoryResult.matches().size());
            return matchHistoryResult;
        } catch (IOException | InterruptedException e) {
            applicationLogger.error("Match History Request to Valve API failed:\n", e);
            throw new ValveException(e);
        }
    }

    public MatchDetailsResult getMatchDetails(long matchId) {
        HttpRequest httpRequest = createGetMatchDetailsRequest(matchId);
        try {
            HttpResponse<String> response = httpClient.doRequest(httpRequest);
            return objectMapper.readValue(response.body(), MatchDetailsResult.class);
        } catch (IOException | InterruptedException e) {
            applicationLogger.error("Match Details Request to services.Valve API failed:\n", e);
            throw new ValveException(e);
        }
    }

    private HttpRequest createGetMatchesRequest(int matchesToRequest) {
        return HttpRequest.newBuilder().GET()
                .uri(buildValveApiUrl(valveSettings.getMatchHistoryPath(),
                        format("game_mode=22&min_players=10&skill=3&matches_requested=%d", matchesToRequest)))
                .build();
    }

    private HttpRequest createGetMatchDetailsRequest(long matchId) {
        return HttpRequest.newBuilder().GET()
                .uri(buildValveApiUrl(valveSettings.getMatchDetailsPath(), format("match_id=%d", matchId)))
                .build();
    }

    private URI buildValveApiUrl(String path, String queryParameters) {
        return URI.create(format("%s%s?key=%s&%s",
                valveSettings.baseValveApiUrl(),
                path,
                valveSettings.valveApiKey(),
                queryParameters));
    }
}
