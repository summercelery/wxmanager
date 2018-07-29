package springboot.core.redis;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *缓存基本操作
 */
@Service
public class JedisService {

    private static Logger logger = LoggerFactory.getLogger(JedisService.class);

    @Autowired
    private JedisPool jedisPool;


    public Jedis getResource(){
        return  jedisPool.getResource();
    }

    private void returnResource(Jedis jedis){
        if (jedis != null) {
            jedis.close();
        }
    }


    public void setWithTimeAndIndex(String key,String value,Integer time,Integer index){
        Jedis jedis = null;
        try{
            jedis = getResource();
            jedis.select(index);
            // NX是不存在时才set， XX是存在时才set， EX是秒，PX是毫秒
            if(jedis.exists(key)){
                jedis.set(key,value,"XX","EX",time);
            }else{
                jedis.set(key,value,"NX","EX",time);
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.error("key:"+key+"set error");
        }finally {
            returnResource(jedis);
        }

    }
    public void set(String key ,String value){
        Jedis jedis = null;
        try{
            jedis = getResource();
            // NX是不存在时才set， XX是存在时才set， EX是秒，PX是毫秒
            if(jedis.exists(key)){
                jedis.set(key,value,"XX");
            }else{
                jedis.set(key,value,"NX");
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.error("key:"+key+"set error");
        }finally {
            returnResource(jedis);
        }
    }


    public Long putMessage(Integer index,String key,String... value){
        Jedis jedis = null;
        try{
            jedis = getResource();
            jedis.select(index);
            return jedis.lpush(key,value);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("key:"+key+"set error");
        }finally {
            returnResource(jedis);
        }

        return null;

    }

    public List<String> getMessage(String key, Integer index){
        Jedis jedis = null;
        try{
            jedis = getResource();
            jedis.select(index);
            return jedis.brpop(0,key);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("key:"+key+"set error");
        }finally {
            returnResource(jedis);
        }
        return null;
    }

    public String getValue(String key){
        Jedis jedis = null;
        try{
            jedis = getResource();
            return jedis.get(key);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("key:"+key+"get error");
        }finally {
            returnResource(jedis);
        }
        return null;
    }

    public Boolean existKey(String key,Integer index){
        Jedis jedis = null;
        try{
            jedis = getResource();
            jedis.select(index);
            return jedis.exists(key);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("key:"+key+"check exist error");
        }finally {
            returnResource(jedis);
        }
        return false;
    }

    public void setHash(String key, Map<String,String> map,Integer index,Integer expireTime){

        Jedis jedis = null;
        try{
            jedis = getResource();
            jedis.select(index);
            jedis.expire(key,expireTime);
            if(!"OK".equals(jedis.hmset(key,map))){
                throw new Exception();
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.error("key:"+key+"check exist error");
        }finally {
            returnResource(jedis);
        }
    }

    public String getHashField(String key,String field,Integer index){

        Jedis jedis = null;
        try{
            jedis = getResource();
            jedis.select(index);
            return jedis.hget(key,field);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("key:"+key+"check exist error");
        }finally {
            returnResource(jedis);
        }
        return null;
    }

    public Set<String> getAllkeys(Integer index){
        Jedis jedis = null;
        try{
            jedis = getResource();
            jedis.select(index);
            return jedis.keys("*");
        }catch (Exception e){
            e.printStackTrace();
            logger.error("get all keys error");
        }finally {
            returnResource(jedis);
        }
        return null;
    }

    public Map<String,String> getAllHash(String key,Integer index){
        Jedis jedis = null;
        try{
            jedis = getResource();
            jedis.select(index);
            return jedis.hgetAll(key);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("get all keys error");
        }finally {
            returnResource(jedis);
        }
        return null;
    }

    public void deleteKey(String key,Integer index){
        Jedis jedis = null;
        try{
            jedis = getResource();
            jedis.select(index);
            jedis.del(key);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("get all keys error");
        }finally {
            returnResource(jedis);
        }
    }
}
