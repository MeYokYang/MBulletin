package top.meyok.user.shiro.cache;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;

/**
 * @author meyok@mbulletin.meyok.top
 * @date 2022/9/2 17:09
 */
public class RedisCacheManager implements CacheManager {

    @Override
    public <K, V> Cache<K, V> getCache(String cacheKey) throws CacheException {
        System.out.println("cacheKey: " + cacheKey);
        return new RedisCache<>(cacheKey);
    }
}
