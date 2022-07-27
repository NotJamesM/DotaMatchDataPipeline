package util;

import domain.valve.MatchDetailsResult;
import domain.valve.MatchRecentHistoryResult;
import domain.valve.PlayerHistory;
import org.slf4j.Logger;
import services.Valve;

import java.util.List;

public class Scraper {

    private final Valve valve;
    private final Logger applicationLogger;

    private static final List<Integer> VALID_LEAVER_STATUES = List.of(0, 1);

    public Scraper(Valve valve, Logger applicationLogger) {
        this.valve = valve;
        this.applicationLogger = applicationLogger;
    }

    public MatchRecentHistoryResult scrapeRecentMatches(int matchesToRequest) {
        final MatchRecentHistoryResult matches = valve.getMatches(matchesToRequest);
        return enrichMatches(matches);
    }

    private MatchRecentHistoryResult enrichMatches(MatchRecentHistoryResult matches) {
//        final int size = matches.matchIds().size();
//        int count = 0;
//        for (Match match : matches.matchIds()) {
//            applicationLogger.info(" [{}/{}] Getting match details for match id: {}", count++, size, match.getMatchId());
//            final MatchDetailsResult matchDetails = valve.getMatchDetails(match.getMatchId());
//            match.setRadiantWin(matchDetails.radiantWin());
//            match.setAbandoned(getAbandonedStatus(matchDetails));
//            match.setGamemode(matchDetails.gameMode());
//            match.setDuration(matchDetails.duration());
//        }
//        return matches;
        return null;
    }

    private boolean getAbandonedStatus(MatchDetailsResult matchDetailsResult) {
        for (PlayerHistory playerHistory : matchDetailsResult.playerHistory()) {
            if (!VALID_LEAVER_STATUES.contains(playerHistory.leaverStatus())) {
                return true;
            }
        }
        return false;
    }

}
