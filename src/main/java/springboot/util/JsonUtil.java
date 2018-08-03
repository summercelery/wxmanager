package springboot.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;

/**
 * json转换工具
 */
public class JsonUtil {

    private static ObjectMapper objectMapper = new ObjectMapper();
    static {
        //序列化时只序列化不为空的
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        //Json序列化增加类别标识就能准确判断子类类型。
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);

        //如果是空对象的时候,不抛异常,也就是对应的属性没有get方法
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    }

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
