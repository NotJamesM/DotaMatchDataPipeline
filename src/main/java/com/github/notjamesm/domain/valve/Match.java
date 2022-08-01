package com.github.notjamesm.domain.valve;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Match implements Comparable<Match> {

    private final long matchId;
    private final long startTime;
    private final long matchSeqNum;
    private final long lobbyType;
    private final List<Player> players;
    private boolean radiantWin;
    private boolean abandoned;
    private long gamemode;
    private long duration;

    @JsonCreator
    public Match(@JsonProperty("match_id") long matchId,
                 @JsonProperty("start_time") long startTime,
                 @JsonProperty("match_seq_num") long matchSeqNumber,
                 @JsonProperty("lobby_type") long lobbyType,
                 @JsonProperty("players") List<Player> players) {
        this.matchId = matchId;
        this.startTime = startTime;
        this.matchSeqNum = matchSeqNumber;
        this.lobbyType = lobbyType;
        this.players = players;
    }

    public long getMatchId() {
        return matchId;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getMatchSeqNum() {
        return matchSeqNum;
    }

    public long getLobbyType() {
        return lobbyType;
    }

    public List<Player> getPlayers() {
        return players;
    }

    @Override
    public int compareTo(Match otherMatch) {
        return Long.compare(this.getMatchId(), otherMatch.getMatchId());
    }

    public void setRadiantWin(boolean radiantWin) {
        this.radiantWin = radiantWin;
    }

    public boolean isRadiantWin() {
        return radiantWin;
    }

    public boolean isAbandoned() {
        return abandoned;
    }

    public void setAbandoned(boolean abandoned) {
        this.abandoned = abandoned;
    }

    public void setGamemode(long gameMode) {
        this.gamemode = gameMode;
    }

    public long getGamemode() {
        return gamemode;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getDuration() {
        return duration;
    }
}
