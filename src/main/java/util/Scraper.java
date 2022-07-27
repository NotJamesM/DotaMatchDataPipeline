package util;

import domain.model.HeroModel;
import domain.model.MatchModel;
import domain.valve.MatchDetailsResult;
import domain.valve.MatchId;
import domain.valve.MatchRecentHistoryResult;
import domain.valve.PlayerHistory;
import org.slf4j.Logger;
import services.Valve;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class Scraper {

    private final Valve valve;
    private final Logger applicationLogger;

    private static final List<Integer> VALID_LEAVER_STATUES = List.of(0, 1);

    public Scraper(Valve valve, Logger applicationLogger) {
        this.valve = valve;
        this.applicationLogger = applicationLogger;
    }

    public List<MatchModel> scrapeRecentMatches(int matchesToRequest) {
        final MatchRecentHistoryResult matches = valve.getMatches(matchesToRequest);
        final List<MatchDetailsResult> matchDetails = getMatchDetails(matches);
        return matchDetailsToMatchModel(matchDetails);
    }

    private List<MatchDetailsResult> getMatchDetails(MatchRecentHistoryResult matches) {
        final List<MatchId> matchIds = matches.matchIds();
        final List<MatchDetailsResult> matchDetailsResults = new ArrayList<>();
        for (int i = 0, matchIdsSize = matchIds.size(); i < matchIdsSize; i++) {
            MatchId matchId = matchIds.get(i);
            applicationLogger.info(" [{}/{}] Getting match details for match id: {}", i++, matchIds.size(), matchId.matchId());
            matchDetailsResults.add(valve.getMatchDetails(matchId.matchId()));
        }
        return matchDetailsResults;
    }

    private List<MatchModel> matchDetailsToMatchModel(List<MatchDetailsResult> matchDetails) {
        return matchDetails.stream()
                .map(this::toMatchModel)
                .collect(toList());
    }

    private MatchModel toMatchModel(MatchDetailsResult matchDetailsResult) {
        return new MatchModel(matchDetailsResult.matchId(),
                matchDetailsResult.lobbyType(),
                matchDetailsResult.gameMode(),
                extractHeroes(matchDetailsResult.players()),
                matchDetailsResult.radiantWin(),
                getAbandonedStatus(matchDetailsResult),
                Duration.ofSeconds(matchDetailsResult.duration()));
    }

    private List<HeroModel> extractHeroes(List<PlayerHistory> players) {
        return players.stream().map(this::toHeroModel).toList();
    }

    private HeroModel toHeroModel(PlayerHistory playerHistory) {
        return new HeroModel(playerHistory.heroId(), playerHistory.teamNumber() == 0);
    }

    private boolean getAbandonedStatus(MatchDetailsResult matchDetailsResult) {
        for (PlayerHistory playerHistory : matchDetailsResult.players()) {
            if (!VALID_LEAVER_STATUES.contains(playerHistory.leaverStatus())) {
                return true;
            }
        }
        return false;
    }

}
