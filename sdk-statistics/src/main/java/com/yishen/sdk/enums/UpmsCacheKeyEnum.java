package com.yishen.sdk.enums;

import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 功能：
 * Charles on 2018/4/18.
 */
public enum UpmsCacheKeyEnum {
	//shiro中用到的所有缓存，key
	CACHE_SHIRO_CACHE("shiro_redis_session:", "RedisCache类的keyPrefix", "RedisCache", "RedisCacheManager"),
	CACHE_CLIENT_SESSION_ID("client-session-id", "局部会话key", "UpmsAuthenticationFilter"),
	CACHE_CLIENT_SESSION_IDS("client-session-ids", "单点同一个code所有局部会话key", "UpmsAuthenticationFilter"),
	CACHE_SERVER_SESSION_ID("server-session-id", "全局会话key", "UpmsRealm", "UpmsSessionDao", "SSOController"),
	CACHE_SERVER_SESSION_IDS("server-session-ids", "全局会话列表key", "UpmsSessionDao", "SSOController"),
	CACHE_SERVER_CODE("server-code", "code key", "UpmsSessionDao", "SSOController"),
	CACHE_SHIRO_SESSION_ID("shiro-session-id", "全局会话key", "UpmsSessionDao"),
	CACHE_UPMSUSER_BY_USERNAME("service:upms_user:by_username", "全局会话key", "UpmsSessionDao"),
	//
	;
	private String   key;//Redis缓存key
	private String   desc;//描述
	private String[] clazzNames;//都有那个类使用

	UpmsCacheKeyEnum(String key, String desc, String... clazzNames) {
		this.key = key;
		this.desc = desc;
		this.clazzNames = clazzNames;
	}

	private static       Logger _log  = LoggerFactory.getLogger(UpmsCacheKeyEnum.class);
	private static final Object _LOCK = new Object();
	private static Map<String,UpmsCacheKeyEnum> _MAP;
	private static List<UpmsCacheKeyEnum>       _ALL_LIST;

	static {
		synchronized (_LOCK) {
			Map<String,UpmsCacheKeyEnum> map = new HashMap<>();
			List<UpmsCacheKeyEnum> listAll = new ArrayList<>();
			for (UpmsCacheKeyEnum e : UpmsCacheKeyEnum.values()) {
				map.put(e.getKey(), e);
				listAll.add(e);
			}
			_MAP = ImmutableBiMap.copyOf(map);
			_ALL_LIST = ImmutableList.copyOf(listAll);
		}
	}

	public UpmsCacheKeyEnum getByKey(String key) {
		return _MAP.get(key);
	}

	public String getKey() {
		return this.key;
	}

	public void setKey(String key) {
		this.key = key;
	}
}
