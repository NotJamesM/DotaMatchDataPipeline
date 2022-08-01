package domain.valve;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT, property = "result", defaultImpl = MatchDetailsResult.class)
public record MatchDetailsResult(
        long matchId,
        long sequenceNumber,
        int lobbyType,
        int gameMode,
        boolean radiantWin,
        long duration,
        List<PlayerHistory> players
) {

    @JsonCreator
    public MatchDetailsResult(@JsonProperty("match_id") long matchId,
                              @JsonProperty("match_seq_num") long sequenceNumber,
                              @JsonProperty("lobby_type") int lobbyType,
                              @JsonProperty("game_mode") int gameMode,
                              @JsonProperty("radiant_win") boolean radiantWin,
                              @JsonProperty("duration") long duration,
                              @JsonProperty("players") List<PlayerHistory> players) {
        this.matchId = matchId;
        this.sequenceNumber = sequenceNumber;
        this.lobbyType = lobbyType;
        this.gameMode = gameMode;
        this.radiantWin = radiantWin;
        this.duration = duration;
        this.players = players;
    }

}
