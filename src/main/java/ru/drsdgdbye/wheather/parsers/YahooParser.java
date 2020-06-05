package ru.drsdgdbye.wheather.parsers;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j;
import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.io.SAXReader;
import ru.drsdgdbye.wheather.Weather;

import java.io.InputStream;

@Log4j
public class YahooParser implements Parser {
    private final Weather weather = Weather.newInstance();

    @Override
    @SneakyThrows
    public Weather parse(InputStream inputStream) {
        log.info("creating xml reader");
        SAXReader xmlReader = new SAXReader(new DocumentFactory());
        Document document = xmlReader.read(inputStream);

        log.info("parsing xml response");
        String rssChannel = "rss/channel/yweather:";
        String rssItem = "rss/channel/item/yweather:";
        weather.setCity(document.valueOf(rssChannel + "location/@city"));
        weather.setCountry(document.valueOf(rssChannel + "location/@country"));
        weather.setCondition(document.valueOf(rssItem + "condition/@text"));
        weather.setTemp(document.valueOf(rssItem + "condition/@temp"));
        weather.setChill(document.valueOf(rssChannel + "wind/@chill"));
        weather.setHumidity(document.valueOf(rssChannel + "atmosphere/@humidity"));

        return weather;
    }
}
