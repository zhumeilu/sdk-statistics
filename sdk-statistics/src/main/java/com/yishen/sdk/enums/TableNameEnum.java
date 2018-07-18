package com.yishen.sdk.enums;

import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yishen.sdk.enums.DBNameEnum.REEXCH;


/**
 * 功能：
 * Charles on 2017/12/6.
 */
public enum TableNameEnum {
	UPMS_LOG(REEXCH, "upms_log"),
	//占位
	;
	private DBNameEnum db;
	private String     name;

	TableNameEnum(DBNameEnum db, String name) {
		this.db = db;
		this.name = name;
	}

	private static       Logger _log  = LoggerFactory.getLogger(TableNameEnum.class);
	private static final Object _LOCK = new Object();
	private static Map<String,TableNameEnum> _MAP;
	private static List<TableNameEnum>       _ALL_LIST;

	static {
		synchronized (_LOCK) {
			Map<String,TableNameEnum> map = new HashMap<>();
			List<TableNameEnum> listAll = new ArrayList<>();
			for (TableNameEnum e : TableNameEnum.values()) {
				map.put(e.getName(), e);
				listAll.add(e);
			}
			_MAP = ImmutableBiMap.copyOf(map);
			_ALL_LIST = ImmutableList.copyOf(listAll);
		}
	}

	public TableNameEnum getByName(String name) {
		return _MAP.get(name);
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public DBNameEnum getDb() {
		return db;
	}

	public void setDb(DBNameEnum db) {
		this.db = db;
	}
}
