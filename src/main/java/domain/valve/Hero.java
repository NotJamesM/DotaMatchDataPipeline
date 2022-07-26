package domain.valve;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_ARRAY, property = "heroes", defaultImpl = Hero.class)
public record Hero(String name, int id, String localisedName) {

    @JsonCreator
    public Hero(@JsonProperty("name") String name,
                @JsonProperty("id") int id,
                @JsonProperty("localized_name") String localisedName) {
        this.name = name;
        this.id = id;
        this.localisedName = localisedName;
    }
}
