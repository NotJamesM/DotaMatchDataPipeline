package domain.model;

import java.time.Duration;
import java.util.List;

public record MatchModel(
        long matchId,
        int lobbyType,
        int gameMode,
        List<HeroModel> heroes,
        boolean radiantWin,
        boolean isAbandoned,
        Duration duration
) {

}
