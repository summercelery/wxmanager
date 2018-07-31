package springboot.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * json转换工具
 */
public class JsonUtil {

    private static ObjectMapper objectMapper = new ObjectMapper();


    /**
     * 类转json
     * @param o
     * @return
     * @throws JsonProcessingException
     */
    public static String objectToJson(Object o) throws JsonProcessingException {

        return objectMapper.writeValueAsString(o);
    }

    /**
     * json转类
     * @param stringObject
     * @param c
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T> T jsonToObject(String stringObject, Class<T> c) throws IOException {
        return objectMapper.readValue(stringObject,c);
    }
}
