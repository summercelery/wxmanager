package springboot.core.redis;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.SimpleSession;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import springboot.util.MapUtil;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import static springboot.constant.RedisConstant.REDIS_SESSION_DB;
import static springboot.constant.RedisConstant.REDIS_SESSION_EXPIRE;

@Repository("redisSessionDAO")
public class RedisSessionDao extends AbstractSessionDAO {

    @Autowired
    private JedisService jedisService;


    @Override
    protected Serializable doCreate(Session session) {

        Serializable sessionId = this.generateSessionId(session);
        this.assignSessionId(session, sessionId);

        jedisService.setHash(sessionId.toString(), MapUtil.objectToMap(session), REDIS_SESSION_DB, REDIS_SESSION_EXPIRE);

        return sessionId;
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        Session session = null;
        Map<String, String> sessonMap = jedisService.getAllHash(sessionId.toString(), REDIS_SESSION_DB);

        try {
            session = MapUtil.mapToObject(sessonMap, SimpleSession.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (null != session && null == session.getId()) {
            return null;
        }
        return session;
    }

    @Override
    public void update(Session session) throws UnknownSessionException {
        if (jedisService.existKey(session.getId().toString(), REDIS_SESSION_DB)) {
            jedisService.setHash(session.getId().toString(), MapUtil.objectToMap(session), REDIS_SESSION_DB, REDIS_SESSION_EXPIRE);
        }

    }

    @Override
    public void delete(Session session) {
        if (jedisService.existKey(session.getId().toString(), REDIS_SESSION_DB)) {
            jedisService.deleteKey(session.getId().toString(), REDIS_SESSION_DB);
        }
    }

    @Override
    public Collection<Session> getActiveSessions() {
        return Collections.emptySet();
    }
}
