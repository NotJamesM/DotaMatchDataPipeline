package com.github.notjamesm.domain.valve;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT, property = "result", defaultImpl = MatchHistoryBySequenceNumberResult.class)
public record MatchHistoryBySequenceNumberResult(List<MatchId> matchIds) {

    @JsonCreator
    public MatchHistoryBySequenceNumberResult(@JsonProperty("matches") List<MatchId> matchIds) {
        this.matchIds = matchIds;
    }
}
