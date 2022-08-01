package com.github.notjamesm.util;

import com.github.notjamesm.domain.model.HeroModel;
import com.github.notjamesm.domain.model.MatchModel;
import com.github.notjamesm.domain.valve.*;
import com.github.notjamesm.services.Valve;
import org.slf4j.Logger;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class Scraper {

    private final Valve valve;
    private final SequenceNumberRepository sequenceNumberRepository;
    private final Logger applicationLogger;

    private static final List<Integer> VALID_LEAVER_STATUES = List.of(0, 1);

    public Scraper(Valve valve, SequenceNumberRepository sequenceNumberRepository, Logger applicationLogger) {
        this.valve = valve;
        this.sequenceNumberRepository = sequenceNumberRepository;
        this.applicationLogger = applicationLogger;
    }

    public List<MatchModel> scrapeRecentMatches(int matchesToRequest) {
        final MatchRecentHistoryResult matches = valve.getMatches(matchesToRequest);
        final List<MatchDetailsResult> matchDetails = getMatchDetails(matches.matchIds());
        return matchDetailsToMatchModel(matchDetails);
    }

    public List<MatchModel> scrapeMatchesBySequenceNumber(int matchesToRequest) {
        return scrapeMatchesBySequenceNumber(matchesToRequest, sequenceNumberRepository.getCurrentSequenceNumber());
    }

    public List<MatchModel> scrapeMatchesBySequenceNumber(int matchesToRequest, long sequenceNumber) {
        final MatchHistoryBySequenceNumberResult matches = valve.getMatchIdsFromSequenceNumber(sequenceNumber, matchesToRequest);
        final List<MatchDetailsResult> matchDetailList = getMatchDetails(matches.matchIds());
        sequenceNumberRepository.updateSequenceNumber(getLatestSequenceNumber(matchDetailList));
        return matchDetailsToMatchModel(matchDetailList);
    }

    private List<MatchDetailsResult> getMatchDetails(List<MatchId> matchIds) {
        final List<MatchDetailsResult> matchDetailsResults = new ArrayList<>();
        for (int i = 0, matchIdsSize = matchIds.size(); i < matchIdsSize; i++) {
            MatchId matchId = matchIds.get(i);
            applicationLogger.info(" [{}/{}] Getting match details for match id: {}", i + 1, matchIds.size(), matchId.matchId());
            matchDetailsResults.add(valve.getMatchDetails(matchId.matchId()));
        }
        return matchDetailsResults;
    }

    private long getLatestSequenceNumber(List<MatchDetailsResult> matchDetailList) {
        return matchDetailList.get(matchDetailList.size() - 1).sequenceNumber();
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
