package ru.drsdgdbye.wheather.parsers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j;
import ru.drsdgdbye.wheather.Weather;

import java.io.InputStream;
import java.util.Map;

@Log4j
public class OpenWeatherMapParser implements Parser {
    Weather weather;

    @Override
    @SneakyThrows
    public Weather parse(InputStream inputStream) {
        log.info("parsing openweathermap");
        ObjectMapper objectMapper = new ObjectMapper();
        weather = objectMapper.readValue(inputStream, Weather.class);
        return weather;
    }
}
