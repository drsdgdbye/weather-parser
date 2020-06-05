package ru.drsdgdbye.wheather.retrievers;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j;
import ru.drsdgdbye.wheather.annotations.InjectProperty;
import ru.drsdgdbye.wheather.configurators.InjectPropertyAnnotationConfigurator;

import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Log4j
public class OpenWeatherMapRetriever implements Retriever {
    private final String openWeatherMapUrl = "https://api.openweathermap.org/data/2.5/weather?q=";
    @InjectProperty("apiKey")
    private String apiKey;

    public OpenWeatherMapRetriever() {
        new InjectPropertyAnnotationConfigurator().configure(this);
    }

    @SneakyThrows
    @Override
    public InputStream retrieve(String location) {
        log.info("retrieving weather data from openweathermap");
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(openWeatherMapUrl + location + "&units=metric" + "&appid=" + apiKey))
                .build();

        HttpResponse<InputStream> response = client
                .send(request, HttpResponse.BodyHandlers.ofInputStream());

        return response.body();
    }
}
