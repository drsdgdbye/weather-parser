package ru.drsdgdbye.wheather.parsers;

import ru.drsdgdbye.wheather.Weather;

import java.io.InputStream;

public interface Parser {
    Weather parse(InputStream inputStream);
}
