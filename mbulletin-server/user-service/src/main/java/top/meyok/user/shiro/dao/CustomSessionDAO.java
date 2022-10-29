package top.meyok.user.shiro.dao;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * @author meyok@mbulletin.meyok.top
 * @date 2022/10/29 13:34
 */
@Repository("customShiroSessionDAO")
public class CustomSessionDAO extends EnterpriseCacheSessionDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomSessionDAO.class);
    @Resource
    private RedisTemplate<String, Object> redisTemplate;


    /**
     * 创建session保存到redis中
     * @param session
     * @return
     */
    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = super.doCreate(session);

        LOGGER.info("create session to redis: SESSIONID={}", sessionId);

        BoundValueOperations<String, Object> sessionValueOperations =
                redisTemplate.boundValueOps("shiro_session_" + sessionId.toString());
        sessionValueOperations.set(session);
        sessionValueOperations.expire(30, TimeUnit.MINUTES);
        return sessionId;
    }


    /**
     * 从redis中获取session
     * @param sessionId
     * @return
     */
    @Override
    protected Session doReadSession(Serializable sessionId) {
        Session session = super.doReadSession(sessionId);

        LOGGER.info("get session from redis: SESSIONID={}", sessionId);

        if (session != null) {
            BoundValueOperations<String, Object> sessionValueOperations =
                    redisTemplate.boundValueOps("shiro_session_" + sessionId.toString());
            session = (Session) sessionValueOperations.get();
        }

        return session;
    }


    /**
     * 更新redis中的session
     * @param session
     */
    @Override
    protected void doUpdate(Session session) {
        super.doUpdate(session);

        LOGGER.info("update session to redis: SESSIONID={}", session.getId());

        BoundValueOperations<String, Object> sessionValueOperations =
                redisTemplate.boundValueOps("shiro_session_" + session.getId().toString());
        sessionValueOperations.set(session);
        sessionValueOperations.expire(30, TimeUnit.MINUTES);

    }


    /**
     * 删除失效session
     * @param session
     */
    @Override
    protected void doDelete(Session session) {

        LOGGER.info("delete session from redis: SESSIONID={}", session.getId());

        redisTemplate.delete("shiro_session_" + session.getId().toString());
        super.doDelete(session);
    }
}
