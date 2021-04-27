package it.unicam.cs.pa.jbudget105126.Model.Persistence;

import com.google.gson.*;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.lang.reflect.Type;

/**
 *  This class is used as type adapter to serialize and deserialize org.joda.time.LocalDate object to/from a Json file
 *
 *  references: https://github.com/gkopff/gson-javatime-serialisers
 */
public class LocalDateSerializer implements JsonDeserializer<LocalDate>, JsonSerializer<LocalDate>
{
    private static final DateTimeFormatter DATE_FORMAT = ISODateTimeFormat.date();

    @Override
    public LocalDate deserialize(final JsonElement je, final Type type,
                                 final JsonDeserializationContext jdc) throws JsonParseException
    {
        final String dateAsString = je.getAsString();
        if (dateAsString.length() == 0)
        {
            return null;
        }
        else
        {
            return DATE_FORMAT.parseLocalDate(dateAsString);
        }
    }

    @Override
    public JsonElement serialize(final LocalDate src, final Type typeOfSrc,
                                 final JsonSerializationContext context)
    {
        String retVal;
        if (src == null)
        {
            retVal = "";
        }
        else
        {
            retVal = DATE_FORMAT.print(src);
        }
        return new JsonPrimitive(retVal);
    }
}