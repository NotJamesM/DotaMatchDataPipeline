package domain.valve;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public record Player(long accountId, int teamSlot, int playerSlot, int teamNumber, int heroId) {

    @JsonCreator
    public Player(@JsonProperty("account_id") long accountId,
                  @JsonProperty("team_slot") int teamSlot,
                  @JsonProperty("player_slot") int playerSlot,
                  @JsonProperty("team_number") int teamNumber,
                  @JsonProperty("hero_id") int heroId) {
        this.accountId = accountId;
        this.teamSlot = teamSlot;
        this.playerSlot = playerSlot;
        this.teamNumber = teamNumber;
        this.heroId = heroId;
    }
}
