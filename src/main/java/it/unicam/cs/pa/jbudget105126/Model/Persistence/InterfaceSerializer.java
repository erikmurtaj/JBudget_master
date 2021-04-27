package it.unicam.cs.pa.jbudget105126.Model.Persistence;

import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * This class is used as generic type adapter to serialize and deserialize object to/from a Json file
 *
 * references: https://stackoverflow.com/questions/41891494/gson-deserialzing-objects-containing-lists
 *
 * @param <T>                               the implementation of the class to serialize/deserialize
 */
final class InterfaceSerializer<T> implements JsonSerializer<T>, JsonDeserializer<T> {

    private final Class<T> implementationClass;

    private InterfaceSerializer(final Class<T> implementationClass) {
        this.implementationClass = implementationClass;
    }

    public static <T> InterfaceSerializer<T> interfaceSerializer(final Class<T> implementationClass) {
        return new InterfaceSerializer<>(implementationClass);
    }

    @Override
    public JsonElement serialize(final T value, final Type type, final JsonSerializationContext context) {
        final Type targetType = value != null
                ? value.getClass()
                : type;
        return context.serialize(value, targetType);
    }

    @Override
    public T deserialize(final JsonElement jsonElement, final Type typeOfT, final JsonDeserializationContext context) {
        return context.deserialize(jsonElement, implementationClass);
    }

}