package com.github.notjamesm.domain.valve;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT, property = "result", defaultImpl = MatchRecentHistoryResult.class)
public record MatchRecentHistoryResult(long remainingResults, List<MatchId> matchIds, long numberOfResults, long status,
                                       long totalResults) {

    @JsonCreator
    public MatchRecentHistoryResult(@JsonProperty("results_remaining") long remainingResults,
                                    @JsonProperty("matches") List<MatchId> matchIds,
                                    @JsonProperty("num_results") long numberOfResults,
                                    @JsonProperty("status") long status,
                                    @JsonProperty("total_results") long totalResults) {
        this.remainingResults = remainingResults;
        this.matchIds = matchIds;
        this.numberOfResults = numberOfResults;
        this.status = status;
        this.totalResults = totalResults;
    }
}
