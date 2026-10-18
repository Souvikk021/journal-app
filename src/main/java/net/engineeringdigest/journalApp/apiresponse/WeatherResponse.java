//package net.engineeringdigest.journalApp.apiresponse;
//
//import com.fasterxml.jackson.annotation.JsonProperty;
//import lombok.Data;
//
//import java.util.ArrayList;
//
//@Data
//public class WeatherResponse {
//
//    private Current current;
//
//    @Data
//    public class Current {
//
//        @JsonProperty("observation_time")
//        private String observationTime;
//        private int temperature;
//        @JsonProperty("weather_descriptions")
//        private ArrayList<String> weatherDescriptions;
//    }
//}



package net.engineeringdigest.journalApp.apiresponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class WeatherResponse {

    private Current current;

    @Data
    public static class Current {

        @JsonProperty("observation_time")
        private String observationTime;

        private int temperature;

        @JsonProperty("weather_descriptions")
        private List<String> weatherDescriptions;
    }
}
