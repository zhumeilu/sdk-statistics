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
 * Charles on 2017/12/6.
 */
public enum DBNameEnum {
	//占位
	REEXCH("reexch-v2"),;
	private String name;

	DBNameEnum(String name) {
		this.name = name;
	}

	private static       Logger _log  = LoggerFactory.getLogger(DBNameEnum.class);
	private static final Object _LOCK = new Object();
	private static Map<String,DBNameEnum> _MAP;
	private static List<DBNameEnum>       _ALL_LIST;

	static {
		synchronized (_LOCK) {
			Map<String,DBNameEnum> map = new HashMap<>();
			List<DBNameEnum> listAll = new ArrayList<>();
			for (DBNameEnum e : DBNameEnum.values()) {
				map.put(e.getName(), e);
				listAll.add(e);
			}
			_MAP = ImmutableBiMap.copyOf(map);
			_ALL_LIST = ImmutableList.copyOf(listAll);
		}
	}

	public DBNameEnum getByName(String name) {
		return _MAP.get(name);
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
