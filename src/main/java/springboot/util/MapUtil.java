package springboot.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import springboot.anno.NotToMap;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class MapUtil {

    private static Logger logger = LoggerFactory.getLogger(MapUtil.class);

    public static Map<String, String> ObjectToMap(Object obj) {

        if (obj == null) {
            return null;
        }
        Map<String, String> map = new HashMap<String, String>();
        ObjectMapper objectMapper = new ObjectMapper();

        try {


            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {


                String key = property.getName();
                // 过滤class属性
                if (!key.equals("class")) {
                    NotToMap notToMap = null;
                    try {
                        notToMap = obj.getClass().getDeclaredField(key).getAnnotation(NotToMap.class);
                    }catch (NoSuchFieldException e){
                        continue;
                    }
                    if (null == notToMap) {
                        Method getter = property.getReadMethod();
                        Object value = getter.invoke(obj);
                        if((value instanceof String)){
                            map.put(key, value.toString());
                        }else{
                            map.put(key,objectMapper.writeValueAsString(value));
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("transBean2Map Error {}", e);
        }
        return map;

    }

    public static Object mapToObject(Map<String, String> map, Class<?> beanClass) throws Exception {
        if (map == null)
            return null;

        ObjectMapper objectMapper = new ObjectMapper();
        Object obj = beanClass.newInstance();

        BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor property : propertyDescriptors) {

            Method setter = property.getWriteMethod();
            if (setter != null) {
                Object o = map.get(property.getName());
                if("java.lang.String".equals(property.getPropertyType().getTypeName())||"java.io.Serializable".equals(property.getPropertyType().getTypeName())){
                    setter.invoke(obj, o);
                }else{
                    if(null != o){
                        setter.invoke(obj,objectMapper.readValue((String) map.get(property.getName()),property.getPropertyType()));
                    }
                }
            }
        }

        return obj;
    }
}
