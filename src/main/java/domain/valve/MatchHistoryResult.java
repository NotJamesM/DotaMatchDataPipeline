package domain.valve;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT, property = "result", defaultImpl = MatchHistoryResult.class)
public record MatchHistoryResult(long remainingResults, List<Match> matches, long numberOfResults, long status,
                                 long totalResults) {

    @JsonCreator
    public MatchHistoryResult(@JsonProperty("results_remaining") long remainingResults,
                              @JsonProperty("matches") List<Match> matches,
                              @JsonProperty("num_results") long numberOfResults,
                              @JsonProperty("status") long status,
                              @JsonProperty("total_results") long totalResults) {
        this.remainingResults = remainingResults;
        this.matches = matches;
        this.numberOfResults = numberOfResults;
        this.status = status;
        this.totalResults = totalResults;
    }
}
