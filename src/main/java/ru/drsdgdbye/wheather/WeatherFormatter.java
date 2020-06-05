package ru.drsdgdbye.wheather;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.util.Objects;

@Log4j
public class WeatherFormatter {
    @SneakyThrows
    public String format(Weather weather) {
        log.info("formatting data");
        Reader reader =
                new InputStreamReader(Objects.requireNonNull(getClass().getClassLoader()
                        .getResourceAsStream("output.vm")));
        VelocityContext context = new VelocityContext();
        context.put("weather", weather );
        StringWriter writer = new StringWriter();
        Velocity.evaluate(context, writer, "", reader);
        return writer.toString();
    }
}
