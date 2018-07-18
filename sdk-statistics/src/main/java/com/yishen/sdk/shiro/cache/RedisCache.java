/**
 *
 */
package com.yishen.sdk.shiro.cache;

import com.yishen.sdk.cache.ICacheClient;
import org.apache.logging.log4j.LogManager;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.CollectionUtils;

import java.util.*;

import static com.yishen.sdk.enums.UpmsCacheKeyEnum.CACHE_SHIRO_CACHE;


/**
 * @author hanhuaiwei
 */
public class RedisCache<K, V> implements Cache<K,V> {

	protected static final org.apache.logging.log4j.Logger _log                         = LogManager.getLogger(RedisCache.class);
	/**
	 * The Redis key prefix for the sessions
	 */
	private        String           keyPrefix = CACHE_SHIRO_CACHE.getKey();
	/**
	 * The wrapped Jedis instance.
	 */
	private ICacheClient cache;

	/**
	 * 通过一个JedisManager实例构造RedisCache
	 */
	public RedisCache(ICacheClient cache) {
		if (cache == null) {
			throw new IllegalArgumentException("Cache argument cannot be null.");
		}
		this.cache = cache;
	}

	/**
	 * Constructs a cache instance with the specified Redis manager and using a
	 * custom key prefix.
	 *
	 * @param cache  The cache manager instance
	 * @param prefix The Redis key prefix
	 */
	public RedisCache(ICacheClient cache, String prefix) {
		this(cache);
		// set the prefix
		this.keyPrefix = prefix;
	}

	/**
	 * 获得String型的key
	 *
	 * @param key
	 * @return
	 */
	private String getStringKey(K key) {
		if (key instanceof String) {
			String preKey = this.keyPrefix + key;
			return preKey;
		} else if (key instanceof PrincipalCollection) {
			String preKey = this.keyPrefix + key.toString();
			return preKey;
		} else {
			return this.keyPrefix + key.toString();
		}
	}

	@Override
	public V get(K key) throws CacheException {
		_log.debug("根据key从Redis中获取对象 key [" + key + "]");
		try {
			if (key == null) {
				return null;
			} else {
				@SuppressWarnings("unchecked")
				V value = (V) cache.get(getStringKey(key));
				return value;
			}
		} catch (Throwable t) {
			throw new CacheException(t);
		}
	}
	//	public String getStr(String key) throws CacheException {
	//		_log.debug("根据key从Redis中获取对象 key [" + key + "]");
	//		try {
	//			if (key == null) {
	//				return null;
	//			} else {
	//				String res = cache.get(this.keyPrefix + key);
	//				return res;
	//			}
	//		} catch (Throwable t) {
	//			throw new CacheException(t);
	//		}
	//	}

	@Override
	public V put(K key, V value) throws CacheException {
		_log.debug("根据key从存储 key [" + key + "]");
		try {
			cache.set(getStringKey(key), value);
			return value;
		} catch (Throwable t) {
			throw new CacheException(t);
		}
	}
	//	public String putStr(String key, String value) throws CacheException {
	//		_log.debug("根据key从存储 key [" + key + "]");
	//		try {
	//			cache.set(this.keyPrefix + key, value);
	//			return value;
	//		} catch (Throwable t) {
	//			throw new CacheException(t);
	//		}
	//	}

	public String put(String key, String value, int expire)
			throws CacheException {
		_log.debug("根据key从存储 key [" + key + "]");
		try {
			cache.set(this.keyPrefix + key, expire, value);
			return value;
		} catch (Throwable t) {
			throw new CacheException(t);
		}
	}
	//	public String removeString(String key) throws CacheException {
	//		_log.debug("从redis中删除 key [" + key + "]");
	//		try {
	//			String previous = cache.get(this.keyPrefix + key);
	//			cache.delete(this.keyPrefix + key);
	//			return previous;
	//		} catch (Throwable t) {
	//			throw new CacheException(t);
	//		}
	//	}

	@Override
	public V remove(K key) throws CacheException {
		_log.debug("从redis中删除 key [" + key + "]");
		try {
			V previous = get(key);
			cache.delete(getStringKey(key));
			return previous;
		} catch (Throwable t) {
			throw new CacheException(t);
		}
	}

	@Override
	public void clear() throws CacheException {
		_log.debug("从redis中删除所有元素");
		try {
			cache.clear(this.keyPrefix + "*");
		} catch (Throwable t) {
			throw new CacheException(t);
		}
	}

	@Override
	public int size() {
		try {
			Long longSize = new Long(cache.size());
			return longSize.intValue();
		} catch (Throwable t) {
			throw new CacheException(t);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<K> keys() {
		try {
			Set<String> keys = cache.keySet(this.keyPrefix + "*");
			if (CollectionUtils.isEmpty(keys)) {
				return Collections.emptySet();
			} else {
				Set<K> newKeys = new HashSet<K>();
				for (String key : keys) {
					newKeys.add((K) key);
				}
				return newKeys;
			}
		} catch (Throwable t) {
			throw new CacheException(t);
		}
	}

	@Override
	public Collection<V> values() {
		try {
			Set<String> keys = cache.keySet(this.keyPrefix + "*");
			if (!CollectionUtils.isEmpty(keys)) {
				List<V> values = new ArrayList<V>(keys.size());
				for (String key : keys) {
					@SuppressWarnings("unchecked")
					V value = get((K) key);
					if (value != null) {
						values.add(value);
					}
				}
				return Collections.unmodifiableList(values);
			} else {
				return Collections.emptyList();
			}
		} catch (Throwable t) {
			throw new CacheException(t);
		}
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
}
