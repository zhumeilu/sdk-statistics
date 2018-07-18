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
 * 功能：properties文件中的key
 * Charles on 2018/4/18.
 */
public enum PropKeyEnum {
	PROP_REMEMBERME_TIMEOUT("lmhd.upms.rememberMe.timeout", "cookie超时时间"),
	PROP_SESSION_TIMEOUT("lmhd.upms.session.timeout", "session超时时间"),
	PROP_SESSION_ID("lmhd.upms.session.id", "sessionid"),
	PROP_SSO_SERVER_URL("lmhd.upms.sso.server.url", "服务登录地址"),
	PROP_APPID("lmhd.upms.appID", ""),
	PROP_TYPE("lmhd.upms.type", ""),
	PROP_OSS_ALIYUN_OSS_POLICY("lmhd.oss.aliyun.oss.policy", ""),
	PROP_SSO_SUCCESS_URL("lmhd.upms.successUrl", ""),;
	private String key;//Redis缓存key
	private String desc;//描述

	PropKeyEnum(String key, String desc) {
		this.key = key;
		this.desc = desc;
	}

	private static       Logger _log  = LoggerFactory.getLogger(PropKeyEnum.class);
	private static final Object _LOCK = new Object();
	private static Map<String,PropKeyEnum> _MAP;
	private static List<PropKeyEnum>       _ALL_LIST;

	static {
		synchronized (_LOCK) {
			Map<String,PropKeyEnum> map = new HashMap<>();
			List<PropKeyEnum> listAll = new ArrayList<>();
			for (PropKeyEnum e : PropKeyEnum.values()) {
				map.put(e.getKey(), e);
				listAll.add(e);
			}
			_MAP = ImmutableBiMap.copyOf(map);
			_ALL_LIST = ImmutableList.copyOf(listAll);
		}
	}

	public PropKeyEnum getByKey(String key) {
		return _MAP.get(key);
	}

	public String getKey() {
		return this.key;
	}

	public void setKey(String key) {
		this.key = key;
	}
}
