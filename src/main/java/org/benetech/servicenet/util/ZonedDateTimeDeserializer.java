package org.benetech.servicenet.util;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import java.lang.reflect.Type;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class ZonedDateTimeDeserializer implements JsonDeserializer<ZonedDateTime> {

    @Override
    public ZonedDateTime deserialize(JsonElement jsonElement, Type type,
        JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonPrimitive jsonPrimitive = jsonElement.getAsJsonPrimitive();
        try {
            return ZonedDateTime.parse(jsonPrimitive.getAsString(), DateTimeFormatter.ISO_ZONED_DATE_TIME);
        } catch (RuntimeException e) {
            throw new JsonParseException("Unable to parse ZonedDateTime", e);
        }
    }
}
