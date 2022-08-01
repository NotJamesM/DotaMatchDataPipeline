package testbuilders;

import domain.valve.PlayerHistory;

public class PlayerBuilder {

    private int heroId;
    private int teamNumber;
    private int leaverStatus;

    public static PlayerBuilder player() {
        return new PlayerBuilder();
    }

    public PlayerBuilder withHeroId(int heroId) {
        this.heroId = heroId;
        return this;
    }

    public PlayerBuilder withTeamNumber(int teamNumber) {
        this.teamNumber = teamNumber;
        return this;
    }

    public PlayerBuilder withLeaverStatus(int leaverStatus) {
        this.leaverStatus = leaverStatus;
        return this;
    }

    public PlayerHistory build() {
        return new PlayerHistory(
                heroId,
                teamNumber,
                leaverStatus
        );
    }
}
