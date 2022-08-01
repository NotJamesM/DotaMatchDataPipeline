package testbuilders;

import domain.valve.MatchDetailsResult;
import domain.valve.PlayerHistory;

import java.util.List;

public class MatchDetailsResultBuilder {

    private long matchId;
    private long sequenceNumber;
    private int lobbyType;
    private int gameMode;
    private boolean radiantWin;
    private long duration;
    private List<PlayerHistory> players;

    public static MatchDetailsResultBuilder matchDetailsResult() {
        return new MatchDetailsResultBuilder();
    }

    public MatchDetailsResultBuilder withMatchId(long matchId) {
        this.matchId = matchId;
        return this;
    }

    public MatchDetailsResultBuilder withSequenceNumber(long sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
        return this;
    }

    public MatchDetailsResultBuilder withLobbyType(int lobbyType) {
        this.lobbyType = lobbyType;
        return this;
    }

    public MatchDetailsResultBuilder withGameMode(int gameMode) {
        this.gameMode = gameMode;
        return this;
    }

    public MatchDetailsResultBuilder withRadiantWin(boolean radiantWin) {
        this.radiantWin = radiantWin;
        return this;
    }

    public MatchDetailsResultBuilder withDuration(long duration) {
        this.duration = duration;
        return this;
    }

    public MatchDetailsResultBuilder withPlayers(List<PlayerHistory> players) {
        this.players = players;
        return this;
    }

    public MatchDetailsResult build() {
        return new MatchDetailsResult(
                matchId,
                sequenceNumber,
                lobbyType,
                gameMode,
                radiantWin,
                duration,
                players
        );
    }
}
