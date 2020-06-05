package ru.drsdgdbye.wheather.parsers;

import ru.drsdgdbye.wheather.retrievers.Retriever;

public class ParserFactory {
    public static Parser getParser(Retriever type) {
        Parser toReturn;
        String className = type.getClass().getSimpleName();

        switch (className) {
            case "YahooRetriever":
                toReturn = new YahooParser();
                break;
            case "OpenWeatherMapRetriever":
                toReturn = new OpenWeatherMapParser();
                break;
            default:
                throw new IllegalArgumentException("wrong retriever");
        }
        return toReturn;
    }

}
