package com.github.notjamesm.domain.valve;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public record PlayerHistory(int heroId, int teamNumber, int leaverStatus) {

    @JsonCreator
    public PlayerHistory(@JsonProperty("hero_id") int heroId,
                         @JsonProperty("team_number") int teamNumber,
                         @JsonProperty("leaver_status") int leaverStatus) {
        this.heroId = heroId;
        this.teamNumber = teamNumber;
        this.leaverStatus = leaverStatus;
    }
}
