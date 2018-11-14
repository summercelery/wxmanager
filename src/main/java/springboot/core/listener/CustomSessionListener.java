package springboot.core.listener;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import springboot.core.core.redis.JedisService;

import static springboot.core.constant.RedisConstant.REDIS_SESSION_DB;


@Configuration
public class CustomSessionListener implements SessionListener {

    private static Logger logger = LoggerFactory.getLogger(CustomSessionListener.class);

    @Autowired
    private JedisService jedisService;

    /**
     * 一个回话的生命周期开始
     */
    @Override
    public void onStart(Session session) {
        logger.info("onStart:{}", session.getId());
    }
    /**
     * 一个回话的生命周期结束
     */
    @Override
    public void onStop(Session session) {
        logger.info("onStop:{}", session.getId());
    }

    @Override
    public void onExpiration(Session session) {
        logger.info("onExpiration:{}", session.getId());
        jedisService.deleteKey(session.getId().toString(),REDIS_SESSION_DB);
    }

}

