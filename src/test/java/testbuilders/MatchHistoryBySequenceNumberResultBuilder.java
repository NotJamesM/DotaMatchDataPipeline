package testbuilders;

import domain.valve.MatchHistoryBySequenceNumberResult;
import domain.valve.MatchId;

import java.util.ArrayList;
import java.util.List;

public class MatchHistoryBySequenceNumberResultBuilder {

    private final List<MatchId> matchIds = new ArrayList<>();

    public static MatchHistoryBySequenceNumberResultBuilder matchHistoryBySequenceNumberResult() {
        return new MatchHistoryBySequenceNumberResultBuilder();
    }

    public MatchHistoryBySequenceNumberResultBuilder addMatchId(long matchId) {
        matchIds.add(new MatchId(matchId));
        return this;
    }

    public MatchHistoryBySequenceNumberResult build() {
        return new MatchHistoryBySequenceNumberResult(matchIds);
    }
}
