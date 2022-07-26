import Settings.ValveSettings;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.github.tomakehurst.wiremock.matching.EqualToPattern;
import domain.valve.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import services.Valve;
import util.ValveFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static java.lang.System.lineSeparator;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@WireMockTest
public class ValveTest {

    private final Logger testLogger = LoggerFactory.getLogger("TEST_LOGGER");
    private final ValveSettings settings = mock(ValveSettings.class);
    private Valve valve;

    @BeforeEach
    void setUp(WireMockRuntimeInfo wireMockRuntimeInfo) {
        when(settings.valveApiKey()).thenReturn("APIKEY");
        when(settings.baseValveApiUrl()).thenReturn(wireMockRuntimeInfo.getHttpBaseUrl() + "/");
        when(settings.rateLimit()).thenReturn(Double.valueOf(1));
        when(settings.getMatchHistoryPath()).thenReturn("IDOTA2Match_570/GetMatchHistory/V001/");
        when(settings.getMatchDetailsPath()).thenReturn("IDOTA2Match_570/GetMatchDetails/V001/");
        valve = new ValveFactory(settings, testLogger).valve();
    }

    @Test
    void shouldGetMatchHistory() throws IOException {
        final String body = String.join(lineSeparator(), Files.readAllLines(Path.of("src/test/resources/apiExamples/matches_response_1_match.json")));
        stubFor(get(urlPathEqualTo("/IDOTA2Match_570/GetMatchHistory/V001/"))
                .withQueryParam("key", new EqualToPattern("APIKEY"))
                .withQueryParam("skill", new EqualToPattern("3"))
                .withQueryParam("matches_requested", new EqualToPattern("1"))
                .withQueryParam("game_mode", new EqualToPattern("22"))
                .willReturn(okJson(body)));

        final MatchHistoryResult matches = valve.getMatches(1);
        final Match match = matches.matches().get(0);
        final List<Player> players = match.getPlayers();

        assertThat(matches.numberOfResults()).isEqualTo(1);
        assertThat(matches.matches().size()).isEqualTo(1);
        assertThat(match.getMatchId()).isEqualTo(6675671621L);
        assertThat(players.size()).isEqualTo(10);
        assertThat(players.get(0).accountId()).isEqualTo(4294967295L);
    }

    @Test
    void shouldGetMatchDetails() throws IOException {
        final String body = String.join(lineSeparator(), Files.readAllLines(Path.of("src/test/resources/apiExamples/matchDetails.json")));
        stubFor(get(urlPathEqualTo("/IDOTA2Match_570/GetMatchDetails/V001/"))
                .withQueryParam("key", new EqualToPattern("APIKEY"))
                .withQueryParam("match_id", new EqualToPattern("6676198789"))
                .willReturn(okJson(body)));

        final MatchDetailsResult matchDetails = valve.getMatchDetails(6676198789L);
        final List<PlayerHistory> playerHistories = matchDetails.playerHistory();

        assertThat(matchDetails.matchId()).isEqualTo(6676198789L);
        assertThat(matchDetails.radiantWin()).isFalse();
        assertThat(playerHistories).hasSize(10);
        assertThat(playerHistories.get(0).leaverStatus()).isEqualTo(0);
    }
}
