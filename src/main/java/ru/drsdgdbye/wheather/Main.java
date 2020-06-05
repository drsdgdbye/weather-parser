package ru.drsdgdbye.wheather;

import org.apache.log4j.BasicConfigurator;
import ru.drsdgdbye.wheather.parsers.Parser;
import ru.drsdgdbye.wheather.parsers.ParserFactory;
import ru.drsdgdbye.wheather.retrievers.OpenWeatherMapRetriever;
import ru.drsdgdbye.wheather.retrievers.Retriever;
import ru.drsdgdbye.wheather.retrievers.RetrieverFactory;
import ru.drsdgdbye.wheather.retrievers.WeatherSupplier;

import java.io.InputStream;

class Main {
    public static void main(String... args) {
        BasicConfigurator.configure();
        var location = args[0];
        WeatherFormatter formatter = new WeatherFormatter();

        Retriever retriever = RetrieverFactory.getRetriever(WeatherSupplier.OPEN_WEATHER_MAP);
        Parser parser = ParserFactory.getParser(retriever);
        InputStream inputStream = retriever.retrieve(location);
        Weather weather = parser.parse(inputStream);
        System.out.println(formatter.format(weather));

        Retriever yahoo = RetrieverFactory.getRetriever(WeatherSupplier.YAHOO);
        Parser yahooParser = ParserFactory.getParser(yahoo);
        InputStream dataInputStreamFromYahoo = yahoo.retrieve(location);
        Weather yahooWeather = yahooParser.parse(dataInputStreamFromYahoo);
        System.out.println(formatter.format(yahooWeather));
    }
}