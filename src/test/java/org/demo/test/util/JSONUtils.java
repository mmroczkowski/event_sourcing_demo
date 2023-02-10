package org.demo.test.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Utility class for JSON conversions.
 *
 * @author mmroczkowski
 */
public class JSONUtils {

    /**
     * Converts the given object to its JSON representation as a string.
     *
     * @param obj the object to convert to JSON
     * @return a string representation of the object in JSON format
     * @throws RuntimeException if an error occurs during the conversion process
     */
    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
