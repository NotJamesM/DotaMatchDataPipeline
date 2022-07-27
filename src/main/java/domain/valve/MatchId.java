package domain.valve;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public record MatchId(long matchId) {

    @JsonCreator
    public MatchId(@JsonProperty("match_id") long matchId) {
        this.matchId = matchId;
    }
}
