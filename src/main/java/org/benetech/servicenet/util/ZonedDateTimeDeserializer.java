package org.benetech.servicenet.util;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import java.lang.reflect.Type;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class ZonedDateTimeDeserializer implements JsonDeserializer<ZonedDateTime> {
    private static final String OLD_HEALTHLEADS_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_ZONED_DATE_TIME;

    public ZonedDateTimeDeserializer() {
    }

    public ZonedDateTimeDeserializer(String dateFormat) {
        this.dateTimeFormatter = DateTimeFormatter.ofPattern(dateFormat, Locale.ENGLISH);
    }

    @SuppressWarnings("PMD.PreserveStackTrace")
    @Override
    public ZonedDateTime deserialize(JsonElement jsonElement, Type type,
        JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonPrimitive jsonPrimitive = jsonElement.getAsJsonPrimitive();
        String dateTimeString = jsonPrimitive.getAsString();

        try {
            return ZonedDateTime.parse(dateTimeString, dateTimeFormatter);
        } catch (RuntimeException e) {
            try {
                DateTimeFormatter rescueDateTimeFormatter = DateTimeFormatter.ofPattern(
                    OLD_HEALTHLEADS_DATE_FORMAT, Locale.ENGLISH);
                return ZonedDateTime.parse(dateTimeString, rescueDateTimeFormatter);
            } catch (RuntimeException e1) {
                throw new JsonParseException("Unable to parse ZonedDateTime", e1);
            }
        }
    }
}
