package ru.drsdgdbye.wheather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor(staticName = "newInstance")
public class Weather {
    @JsonSetter("name")
    private String city;
    private String country;
    private String condition;
    private String temp;
    private String chill;
    private String humidity;

    //insert methods need for parsing inner data structures with jackson.
    // not very elegant, but is better than to add one more library

    @JsonProperty("sys")
    public void insertCountry(Map<String, String> jsonMap) {
        this.country = jsonMap.get("country");
    }

    @JsonProperty("main")
    public void insertTemp(Map<String, String> jsonMap) {
        this.temp = jsonMap.get("temp");
        this.humidity = jsonMap.get("humidity");
    }

    @JsonProperty("wind")
    public void insertChill(Map<String, String> jsonMap) {
        this.chill = "Speed: " + jsonMap.get("speed");
    }

    @JsonProperty("weather")
    public void insertCondition(List<Map<String, String>> jsonList) {
        this.condition = jsonList.get(0).get("main");
    }
}