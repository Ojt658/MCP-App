package application;
import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * A class that identifies different types of vehicle in the car-park data when reading from a JSON file
 * @param <T> an Object input
 */

public class JsonDeserialization<T> implements JsonDeserializer<T> {
    /**
     * The method that deserialises the different types of vehicle to their correct class
     *
     * @param json becomes a JsonObject
     * @param context is used to deserialise the object
     * @return The deserialised object to the correct class
     * @throws JsonParseException
     */
    @Override
    public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        JsonPrimitive classNamePrimitive = (JsonPrimitive) jsonObject.get("type"); // Find the type of vehicle

        String className = classNamePrimitive.getAsString(); // Gets the value of type

        Class<?> clazz;
        try {
            clazz = Class.forName(className); // Set the class name to the type from the file
        } catch (ClassNotFoundException e) {
            throw new JsonParseException(e.getMessage());
        }
        return context.deserialize(jsonObject, clazz); // The deserialised object to the correct class
    }

}