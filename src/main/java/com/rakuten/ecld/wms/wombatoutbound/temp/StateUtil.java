package com.rakuten.ecld.wms.wombatoutbound.temp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class StateUtil {
    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        // Omit the properties that exist in json string but not in java pojo
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
    }

    public static Object deserializeState(JsonNode stateData, Object state) {
        Object result = null;
        if (stateData != null) {
            try {
                result = mapper.treeToValue(stateData, state.getClass());
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Failed to deserialize state", e);
            }
        }
        return result;
    }

    public static JsonNode serialize(Object state) {
        return mapper.valueToTree(state);
    }

    public static <T> T readValue(String value, Class<T> clazz){
        try {
            return mapper.readValue(value, clazz);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] writeValueAsBytes(Object value){
        try {
            return mapper.writeValueAsBytes(value);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
