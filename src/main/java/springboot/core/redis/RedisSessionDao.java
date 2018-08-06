package springboot.core.redis;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import static springboot.constant.RedisConstant.REDIS_SESSION_EXPIRE;

/**
 * 放弃使用jedis的使用自定义的json方式处理redis中的session，直接使用redisTemplate的jdk序列化方式
 */
@Repository("redisSessionDAO")
public class RedisSessionDao extends AbstractSessionDAO {

    private static Logger logger = LoggerFactory.getLogger(RedisSessionDao.class);

//    @Autowired
//    private JedisService jedisService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    @Override
    protected Serializable doCreate(Session session) {

        Serializable sessionId = this.generateSessionId(session);
        this.assignSessionId(session, sessionId);

//        try {
//            jedisService.setWithTimeAndIndex(sessionId.toString(), JsonUtil.objectToJson(session),REDIS_SESSION_EXPIRE, REDIS_SESSION_DB);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
        logger.debug("创建session："+session.getId().toString());
        redisTemplate.opsForValue().set(sessionId.toString(), session,REDIS_SESSION_EXPIRE, TimeUnit.SECONDS);


        return sessionId;
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
//        String sessionString = jedisService.getValue(sessionId.toString(),REDIS_SESSION_DB);
//        Session session = null;
//        try {
//            if(StringUtils.isNotBlank(sessionString)){
//                session = JsonUtil.jsonToObject(sessionString,SimpleSession.class);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        logger.debug("读取session："+sessionId.toString());

        return (Session) redisTemplate.opsForValue().get(sessionId.toString());
    }

    @Override
    public void update(Session session) throws UnknownSessionException {
//        if (jedisService.existKey(session.getId().toString(), REDIS_SESSION_DB)) {
//            try {
//                jedisService.setWithTimeAndIndex(session.getId().toString(), JsonUtil.objectToJson(session),REDIS_SESSION_EXPIRE, REDIS_SESSION_DB);
//            } catch (JsonProcessingException e) {
//                e.printStackTrace();
//            }
//        }
        logger.debug("更新session："+session.getId().toString());
        redisTemplate.opsForValue().set(session.getId().toString(), session,REDIS_SESSION_EXPIRE, TimeUnit.SECONDS);


    }

    @Override
    public void delete(Session session) {
//        if (jedisService.existKey(session.getId().toString(), REDIS_SESSION_DB)) {
//            jedisService.deleteKey(session.getId().toString(), REDIS_SESSION_DB);
//        }
        logger.debug("删除session："+session.getId().toString());
        redisTemplate.delete(session.getId().toString());

    }

    @Override
    public Collection<Session> getActiveSessions() {
        return Collections.emptySet();
    }
}
