package domain.valve;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public record PlayerHistory(int leaverStatus) {

    @JsonCreator
    public PlayerHistory(@JsonProperty("leaver_status") int leaverStatus) {
        this.leaverStatus = leaverStatus;
    }
}
