/**
 *
 */
package com.yishen.sdk.shiro.cache;

import com.yishen.sdk.cache.ICacheClient;
import org.apache.logging.log4j.LogManager;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.util.Destroyable;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static com.yishen.sdk.enums.UpmsCacheKeyEnum.CACHE_SHIRO_CACHE;


/**
 * @author hanhuaiwei
 */
public class RedisCacheManager implements CacheManager, Destroyable {

	protected static final org.apache.logging.log4j.Logger _log   = LogManager.getLogger(RedisCacheManager.class);
	// fast lookup by name map
	@SuppressWarnings("rawtypes")
	private final          ConcurrentMap<String,Cache>     caches = new ConcurrentHashMap<String,Cache>();
	@Autowired
	private ICacheClient redisManager;
	/**
	 * The Redis key prefix for caches
	 */
	private String keyPrefix = CACHE_SHIRO_CACHE.getKey();

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public <K, V> Cache<K,V> getCache(String name) throws CacheException {
		_log.debug("获取名称为: " + name + " 的RedisCache实例");
		Cache c = caches.get(name);
		if (c == null) {
			// create a new cache instance
			c = new RedisCache<K,V>(redisManager, keyPrefix);
			// add it to the cache collection
			caches.put(name, c);
		}
		return c;
	}

	public ICacheClient getRedisManager() {
		return redisManager;
	}

	public void setRedisManager(ICacheClient redisManager) {
		this.redisManager = redisManager;
	}

	/**
	 * Returns the Redis session keys prefix.
	 *
	 * @return The prefix
	 */
	public String getKeyPrefix() {
		return keyPrefix;
	}

	/**
	 * Sets the Redis sessions key prefix.
	 *
	 * @param keyPrefix The prefix
	 */
	public void setKeyPrefix(String keyPrefix) {
		this.keyPrefix = keyPrefix;
	}

	@Override
	public void destroy() throws Exception {
		this.redisManager.destroy();
	}
}
