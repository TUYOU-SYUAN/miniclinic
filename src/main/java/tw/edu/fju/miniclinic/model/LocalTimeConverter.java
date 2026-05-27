package tw.edu.fju.miniclinic.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Converter
public class LocalTimeConverter implements AttributeConverter<LocalTime, String> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Override
    public String convertToDatabaseColumn(LocalTime time) {
        return time == null ? null : time.format(FORMATTER); // LocalTime -> "HH:mm:ss"
    }

    @Override
    public LocalTime convertToEntityAttribute(String s) {
        return s == null ? null : LocalTime.parse(s, FORMATTER); // "HH:mm:ss" -> LocalTime
    }
}