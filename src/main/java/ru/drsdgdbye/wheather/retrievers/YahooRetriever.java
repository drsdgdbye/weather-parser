package ru.drsdgdbye.wheather.retrievers;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j;
import ru.drsdgdbye.wheather.annotations.InjectProperty;
import ru.drsdgdbye.wheather.configurators.InjectPropertyAnnotationConfigurator;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.InputStream;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;

@Log4j
public class YahooRetriever implements Retriever {
    private final String YAHOO_WEATHER_URL = "https://weather-ydn-yql.media.yahoo.com/forecastrss";
    @InjectProperty("appId")
    private String appId;
    @InjectProperty("consumerKey")
    private String consumerKey;
    @InjectProperty("consumerSecret")
    private String consumerSecret;
    private String location;

    public YahooRetriever() {
        new InjectPropertyAnnotationConfigurator().configure(this);
    }

    @Override
    @SneakyThrows
    public InputStream retrieve(String location) {
        this.location = formatArg(location);

        String authorizationLine = createAuthorizationLine();

        log.info("retrieving weather data");
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(YAHOO_WEATHER_URL + "?location=" + this.location + "&u=c"))
                .header("Authorization", authorizationLine)
                .header("X-Yahoo-App-Id", appId)
                .header("Content-Type", "text/xml")
                .build();

        HttpResponse<InputStream> response = client
                .send(request, HttpResponse.BodyHandlers.ofInputStream());

        return response.body();
    }

    private String formatArg(String arg) {
        return arg == null ? "moscow,ru" : arg.toLowerCase().trim();
    }

    @SneakyThrows
    private String createAuthorizationLine() {
        log.info("create authorization");
        long timestamp = Instant.now().getEpochSecond();
        byte[] nonce = new byte[32];
        Random rand = new Random();
        rand.nextBytes(nonce);
        String oauthNonce = new String(nonce).replaceAll("\\W", "");

        List<String> parameters = new ArrayList<>();
        parameters.add("oauth_consumer_key=" + consumerKey);
        parameters.add("oauth_nonce=" + oauthNonce);
        parameters.add("oauth_signature_method=HMAC-SHA1");
        parameters.add("oauth_timestamp=" + timestamp);
        parameters.add("oauth_version=1.0");
        parameters.add("location=" + URLEncoder.encode(location, StandardCharsets.UTF_8));
        parameters.add("u=c");
        Collections.sort(parameters);

        StringBuilder parametersList = new StringBuilder();
        for (int i = 0; i < parameters.size(); i++) {
            parametersList.append((i > 0) ? "&" : "").append(parameters.get(i));
        }

        String signatureString = "GET&" +
                URLEncoder.encode(YAHOO_WEATHER_URL, StandardCharsets.UTF_8) + "&" +
                URLEncoder.encode(parametersList.toString(), StandardCharsets.UTF_8);

        SecretKeySpec signingKey = new SecretKeySpec((consumerSecret + "&").getBytes(), "HmacSHA1");
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(signingKey);
        byte[] rawHMAC = mac.doFinal(signatureString.getBytes());
        Base64.Encoder encoder = Base64.getEncoder();
        String signature = encoder.encodeToString(rawHMAC);

        return "OAuth " +
                "oauth_consumer_key=\"" + consumerKey + "\", " +
                "oauth_nonce=\"" + oauthNonce + "\", " +
                "oauth_timestamp=\"" + timestamp + "\", " +
                "oauth_signature_method=\"HMAC-SHA1\", " +
                "oauth_signature=\"" + signature + "\", " +
                "oauth_version=\"1.0\"";
    }
}
