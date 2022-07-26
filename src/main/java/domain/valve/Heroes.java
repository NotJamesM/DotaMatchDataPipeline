package domain.valve;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_ARRAY, property = "heroes", defaultImpl = Heroes.class)
public record Heroes(List<Hero> heroes) {

    @JsonCreator
    public Heroes(@JsonProperty("heroes") List<Hero> heroes) {
        this.heroes = heroes;
    }
}
