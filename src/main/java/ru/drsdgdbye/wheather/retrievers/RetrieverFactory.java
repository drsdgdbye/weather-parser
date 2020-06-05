package ru.drsdgdbye.wheather.retrievers;

public class RetrieverFactory {

    public static Retriever getRetriever(WeatherSupplier type) {
        Retriever toReturn;

        switch (type) {
            case YAHOO:
                toReturn = new YahooRetriever();
                break;
            case OPEN_WEATHER_MAP:
                toReturn = new OpenWeatherMapRetriever();
                break;
            default:
                throw new IllegalArgumentException("wrong type weather supplier");
        }
        return toReturn;
    }

}
