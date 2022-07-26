package util;

import domain.valve.Match;
import domain.valve.MatchDetailsResult;
import domain.valve.MatchHistoryResult;
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

    public MatchHistoryResult scrapeRecentMatches(int matchesToRequest) {
        final MatchHistoryResult matches = valve.getMatches(matchesToRequest);
        return enrichMatches(matches);
    }

    private MatchHistoryResult enrichMatches(MatchHistoryResult matches) {
        final int size = matches.matches().size();
        int count = 0;
        for (Match match : matches.matches()) {
            applicationLogger.info(" [{}/{}] Getting match details for match id: {}", count++, size, match.getMatchId());
            final MatchDetailsResult matchDetails = valve.getMatchDetails(match.getMatchId());
            match.setRadiantWin(matchDetails.radiantWin());
            match.setAbandoned(getAbandonedStatus(matchDetails));
            match.setGamemode(matchDetails.gameMode());
            match.setDuration(matchDetails.duration());
        }
        return matches;
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
