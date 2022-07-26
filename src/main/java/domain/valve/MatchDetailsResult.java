package domain.valve;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT, property = "result", defaultImpl = MatchDetailsResult.class)
public record MatchDetailsResult(
        long matchId,
        long lobbyType,
        long gameMode,
        boolean radiantWin,
        long duration,
        List<PlayerHistory> playerHistory
) {

    @JsonCreator
    public MatchDetailsResult(@JsonProperty("match_id") long matchId,
                              @JsonProperty("lobby_type") long lobbyType,
                              @JsonProperty("game_mode") long gameMode,
                              @JsonProperty("radiant_win") boolean radiantWin,
                              @JsonProperty("duration") long duration,
                              @JsonProperty("players") List<PlayerHistory> playerHistory) {
        this.matchId = matchId;
        this.lobbyType = lobbyType;
        this.gameMode = gameMode;
        this.radiantWin = radiantWin;
        this.duration = duration;
        this.playerHistory = playerHistory;
    }
}
