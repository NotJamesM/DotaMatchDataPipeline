package util;

import domain.model.HeroModel;
import domain.model.MatchModel;
import domain.valve.MatchDetailsResult;
import domain.valve.MatchId;
import domain.valve.MatchRecentHistoryResult;
import domain.valve.PlayerHistory;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import services.Valve;

import java.time.Duration;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static testbuilders.MatchDetailsResultBuilder.matchDetailsResult;
import static testbuilders.MatchHistoryBySequenceNumberResultBuilder.matchHistoryBySequenceNumberResult;
import static testbuilders.PlayerBuilder.player;

class ScraperTest implements WithAssertions {

    @Test
    void scraperShouldReturnListOfDetailedMatches() {
        when(valve.getMatches(1)).thenReturn(new MatchRecentHistoryResult(0, List.of(new MatchId(MATCH_ID)), 1, 1, 1));
        when(valve.getMatchDetails(MATCH_ID)).thenReturn(new MatchDetailsResult(MATCH_ID, 3333444L, 22, 12, true, _32_MINUTES, players));
        final List<MatchModel> matchModels = scraper.scrapeRecentMatches(1);
        final MatchModel model = matchModels.get(0);
        assertThat(matchModels).hasSize(1);
        assertThatMatchDetailsAreCorrect(model);
        assertThatHeroesAreCorrect(model);
    }

    @Test
    void scraperSequenceNumberTest() {
        when(sequenceNumberRepository.getCurrentSequenceNumber()).thenReturn(4500000000L);
        when(valve.getMatchIdsFromSequenceNumber(eq(4500000000L), anyInt()))
                .thenReturn(matchHistoryBySequenceNumberResult()
                        .addMatchId(123456L)
                        .addMatchId(123457L)
                        .addMatchId(123458L)
                        .build());
        primeMatchDetails();

        final List<MatchModel> matchModels = scraper.scrapeMatchesBySequenceNumber(3);
        assertThat(matchModels).hasSize(3);
        assertThat(matchModels).extracting("matchId").containsExactlyInAnyOrder(123456L, 123457L, 123458L);
        verify(sequenceNumberRepository).updateSequenceNumber(5500000000L);
    }

    private void primeMatchDetails() {
        when(valve.getMatchDetails(123456L))
                .thenReturn(matchDetailsResult()
                        .withMatchId(123456L)
                        .withSequenceNumber(4500000001L)
                        .withPlayers(List.of(player().withHeroId(1).withLeaverStatus(0).withTeamNumber(1).build())).build());
        when(valve.getMatchDetails(123457L))
                .thenReturn(matchDetailsResult()
                        .withMatchId(123457L)
                        .withSequenceNumber(4500000002L)
                        .withPlayers(List.of(player().withHeroId(1).withLeaverStatus(0).withTeamNumber(1).build())).build());
        when(valve.getMatchDetails(123458L))
                .thenReturn(matchDetailsResult()
                        .withMatchId(123458L)
                        .withSequenceNumber(5500000000L)
                        .withPlayers(List.of(player().withHeroId(1).withLeaverStatus(0).withTeamNumber(1).build())).build());
    }

    @BeforeEach
    void setUp() {
        scraper = new ScraperFactory(valve, sequenceNumberRepository, logger).scraper();
    }

    private void assertThatMatchDetailsAreCorrect(MatchModel model) {
        assertThat(model.matchId()).isEqualTo(MATCH_ID);
        assertThat(model.duration().getSeconds()).isEqualTo(_32_MINUTES);
        assertThat(model.isAbandoned()).isEqualTo(true);
        assertThat(model.gameMode()).isEqualTo(12);
        assertThat(model.lobbyType()).isEqualTo(22);
    }

    private void assertThatHeroesAreCorrect(MatchModel model) {
        assertThat(model.heroes()).hasSize(10);
        assertThat(model.heroes()).isEqualTo(heroModels);
    }

    public static final long MATCH_ID = 1234567L;
    public static final long _32_MINUTES = Duration.ofMinutes(32L).toSeconds();
    private final Logger logger = LoggerFactory.getLogger("TEST_LOGGER");
    private final Valve valve = mock(Valve.class);
    private final SequenceNumberRepository sequenceNumberRepository = mock(SequenceNumberRepository.class);
    private Scraper scraper;

    private final List<PlayerHistory> players = List.of(
            new PlayerHistory(1, 0, 0),
            new PlayerHistory(2, 0, 0),
            new PlayerHistory(3, 0, 0),
            new PlayerHistory(4, 0, 5),
            new PlayerHistory(5, 0, 0),
            new PlayerHistory(6, 1, 0),
            new PlayerHistory(7, 1, 0),
            new PlayerHistory(8, 1, 0),
            new PlayerHistory(9, 1, 0),
            new PlayerHistory(10, 1, 0)
    );
    private final List<HeroModel> heroModels = List.of(
            new HeroModel(1, true),
            new HeroModel(2, true),
            new HeroModel(3, true),
            new HeroModel(4, true),
            new HeroModel(5, true),
            new HeroModel(6, false),
            new HeroModel(7, false),
            new HeroModel(8, false),
            new HeroModel(9, false),
            new HeroModel(10, false)
    );
}