package ru.drsdgdbye.wheather.retrievers;

import java.io.InputStream;

public interface Retriever {
    InputStream retrieve(String location);
}
