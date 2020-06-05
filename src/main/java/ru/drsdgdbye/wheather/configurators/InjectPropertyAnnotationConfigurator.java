package ru.drsdgdbye.wheather.configurators;

import lombok.SneakyThrows;
import ru.drsdgdbye.wheather.annotations.InjectProperty;

import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

public class InjectPropertyAnnotationConfigurator implements AnnotationConfigurator {
    private final Map<String, String> propertiesMap;

    @SneakyThrows
    public InjectPropertyAnnotationConfigurator() {
        String path = Objects.requireNonNull(ClassLoader.getSystemClassLoader()
                .getResource("application.properties"))
                .getPath();

        Stream<String> lines = new BufferedReader(new FileReader(path)).lines();
        propertiesMap = lines.map(l -> l.split("="))
                .collect(toMap(arr -> arr[0], arr -> arr[1]));
    }

    @Override
    @SneakyThrows
    public void configure(Object o) {
        Class<?> implClass = o.getClass();

        for (Field field : implClass.getDeclaredFields()) {
            InjectProperty annotation = field.getAnnotation(InjectProperty.class);

            if (annotation != null) {
                String value = annotation.value().isEmpty() ? propertiesMap.get(field.getName()) : propertiesMap.get(annotation.value());
                field.setAccessible(true);
                field.set(o, value);
            }
        }
    }
}
