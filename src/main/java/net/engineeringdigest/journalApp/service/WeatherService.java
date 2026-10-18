package net.engineeringdigest.journalApp.service;

import lombok.extern.slf4j.Slf4j;
import net.engineeringdigest.journalApp.apiresponse.WeatherResponse;
import net.engineeringdigest.journalApp.cache.AppCache;
import net.engineeringdigest.journalApp.constants.PlaceHolders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class WeatherService {
    @Value("${weather.api.key}")
    private String apiKey;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AppCache appCache;

    @Autowired
    private RedisService redisService;

    public WeatherResponse getWeather(String city) {
        String cacheKey = "weather_of_" + city;

        WeatherResponse cached =
                redisService.get(cacheKey, WeatherResponse.class);

        if (cached != null && cached.getCurrent() != null) {
            return cached;
        }

        String finalAPI = appCache.appCache
                .get(AppCache.keys.WEATHER_API.toString())
                .replace(PlaceHolders.CITY, city)
                .replace(PlaceHolders.API_KEY, apiKey);



        ResponseEntity<WeatherResponse> response =
                restTemplate.exchange(
                        finalAPI,
                        HttpMethod.GET,
                        null,
                        WeatherResponse.class
                );

        WeatherResponse body = response.getBody();

        if (body == null || body.getCurrent() == null) {
            log.error(
                    "Weather API returned no current weather data for city: {}",
                    city
            );
            return null;
        }

        redisService.set(cacheKey, body, 300L);
        return body;
    }


//    public WeatherResponse getWeather(String city){
//        WeatherResponse weatherResponse = redisService.get("weather_of_" + city, WeatherResponse.class);
//        if(weatherResponse != null){
//            return weatherResponse;
//        }else{
//            String finalAPI = appCache.appCache.get(AppCache.keys.WEATHER_API.toString())
//                    .replace(PlaceHolders.CITY, city)
//                    .replace(PlaceHolders.API_KEY, apiKey);
//            ResponseEntity<WeatherResponse> response =
//                    restTemplate.exchange(finalAPI, HttpMethod.GET, null, WeatherResponse.class);
//            WeatherResponse body = response.getBody();
//            if(body != null){
//                redisService.set("weather_of_" + city, body, 300l);
//            }
//            return body;
//        }
//    }
}
