
package springboot.core.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class CacheUtils {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private static RedisTemplate<String, Object> staticRedisTemplate;

    @PostConstruct
    public void init() {
        staticRedisTemplate = redisTemplate;
    }

    /**
     * @param key
     * @return
     */
    public static Object get(String key) {
        return staticRedisTemplate.opsForValue().get(key);
    }

    /**
     * 写入WX_CACHE缓存
     *
     * @param key
     * @return
     */
    public static void put(String key, Object value) {
        staticRedisTemplate.opsForValue().set(key, value);
    }

    /**
     * 从WX_CACHE缓存中移除
     *
     * @param key
     * @return
     */
    public static void remove(String key) {

        staticRedisTemplate.delete(key);
    }


}
