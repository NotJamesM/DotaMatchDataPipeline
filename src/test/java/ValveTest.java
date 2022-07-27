import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.github.tomakehurst.wiremock.matching.EqualToPattern;
import domain.valve.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import services.Valve;
import settings.ValveSettings;
import util.ValveFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static java.lang.System.lineSeparator;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@WireMockTest
public class ValveTest {

    private final Logger testLogger = LoggerFactory.getLogger("TEST_LOGGER");
    private final ValveSettings settings = mock(ValveSettings.class);
    private Valve valve;

    @Test
    void matchBySeqNumberShouldReturnListOfMatchIds() throws IOException {
        stubFor(get(urlPathEqualTo("/IDOTA2Match_570/GetMatchHistoryBySequenceNum/V001/"))
                .withQueryParam("key", new EqualToPattern("APIKEY"))
                .withQueryParam("matches_requested", new EqualToPattern("100"))
                .withQueryParam("start_at_match_seq_num", new EqualToPattern("5584240754"))
                .willReturn(okJson(getApiExampleJson("match_by_seq_number.json"))));

        final List<MatchId> expectedMatchIds = Stream.of(6677243344L, 6677196925L, 6677232182L, 6677197160L, 6677185464L, 6677228827L, 6677214592L, 6677211996L, 6677230785L, 6677212191L, 6677207199L, 6677228671L, 6677203659L, 6677209645L, 6677194983L, 6677252037L, 6677225086L, 6677226516L, 6677214261L, 6677201349L).map(MatchId::new)
                .collect(toList());
        final List<MatchId> actualMatchIds = valve.getMatchIdsFromSequenceNumber(5584240754L).matchIds();

        assertThat(actualMatchIds).hasSize(20);
        assertThat(actualMatchIds).isEqualTo(expectedMatchIds);
    }

    @Test
    void shouldGetMatchHistory() throws IOException {
        stubFor(get(urlPathEqualTo("/IDOTA2Match_570/GetMatchHistory/V001/"))
                .withQueryParam("key", new EqualToPattern("APIKEY"))
                .withQueryParam("skill", new EqualToPattern("3"))
                .withQueryParam("matches_requested", new EqualToPattern("1"))
                .withQueryParam("game_mode", new EqualToPattern("22"))
                .willReturn(okJson(getApiExampleJson("matches_response_1_match.json"))));

        final MatchHistoryResult matchHistoryResult = valve.getMatches(1);
        final Match match = matchHistoryResult.matches().get(0);
        final List<Player> players = match.getPlayers();

        assertThat(matchHistoryResult.numberOfResults()).isEqualTo(1);
        assertThat(matchHistoryResult.matches().size()).isEqualTo(1);
        assertThat(match.getMatchId()).isEqualTo(6675671621L);
        assertThat(players.size()).isEqualTo(10);
        assertThat(players.get(0).accountId()).isEqualTo(4294967295L);
    }

    @Test
    void shouldGetMatchDetails() throws IOException {
        stubFor(get(urlPathEqualTo("/IDOTA2Match_570/GetMatchDetails/V001/"))
                .withQueryParam("key", new EqualToPattern("APIKEY"))
                .withQueryParam("match_id", new EqualToPattern("6676198789"))
                .willReturn(okJson(getApiExampleJson("matchDetails.json"))));

        final MatchDetailsResult matchDetails = valve.getMatchDetails(6676198789L);
        final List<PlayerHistory> playerHistories = matchDetails.playerHistory();

        assertThat(matchDetails.matchId()).isEqualTo(6676198789L);
        assertThat(matchDetails.radiantWin()).isFalse();
        assertThat(playerHistories).hasSize(10);
        assertThat(playerHistories.get(0).leaverStatus()).isEqualTo(0);
    }

    @BeforeEach
    void setUp(WireMockRuntimeInfo wireMockRuntimeInfo) {
        when(settings.valveApiKey()).thenReturn("APIKEY");
        when(settings.baseValveApiUrl()).thenReturn(wireMockRuntimeInfo.getHttpBaseUrl() + "/");
        when(settings.rateLimit()).thenReturn(Double.valueOf(1));
        when(settings.getMatchHistoryPath()).thenReturn("IDOTA2Match_570/GetMatchHistory/V001/");
        when(settings.getMatchDetailsPath()).thenReturn("IDOTA2Match_570/GetMatchDetails/V001/");
        when(settings.getMatchHistoryBySeqNumPath()).thenReturn("IDOTA2Match_570/GetMatchHistoryBySequenceNum/V001/");
        valve = new ValveFactory(settings, testLogger).valve();
    }

    private String getApiExampleJson(String apiExampleFileName) throws IOException {
        return String.join(lineSeparator(), Files.readAllLines(Path.of("src/test/resources/apiExamples/" + apiExampleFileName)));
    }
}
