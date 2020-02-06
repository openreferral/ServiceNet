package org.benetech.servicenet.util;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import java.lang.reflect.Type;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class ZonedDateTimeDeserializer implements JsonDeserializer<ZonedDateTime> {
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_ZONED_DATE_TIME;

    public ZonedDateTimeDeserializer() {
    }

    public ZonedDateTimeDeserializer(String dateFormat) {
        this.dateTimeFormatter = DateTimeFormatter.ofPattern(dateFormat, Locale.ENGLISH);
    }

    @Override
    public ZonedDateTime deserialize(JsonElement jsonElement, Type type,
        JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonPrimitive jsonPrimitive = jsonElement.getAsJsonPrimitive();
        String dateTimeString = jsonPrimitive.getAsString();

        try {
            return ZonedDateTime.parse(dateTimeString, dateTimeFormatter);
        } catch (RuntimeException e) {
            try {
                DateTimeFormatter dateTimeFormatterWithoutZome = DateTimeFormatter
                    .ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
                dateTimeFormatterWithoutZome = dateTimeFormatterWithoutZome.withZone(ZoneId.of("UTC"));
                return ZonedDateTime.parse(dateTimeString, dateTimeFormatterWithoutZome);
            } catch (RuntimeException e1) {
                throw new JsonParseException("Unable to parse ZonedDateTime", e1);
            }
        }
    }
}
