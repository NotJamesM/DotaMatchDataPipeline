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
) implements Comparable<MatchModel> {

    @Override
    public int compareTo(MatchModel otherMatchModel) {
        return Long.compare(matchId, otherMatchModel.matchId);
    }
}
