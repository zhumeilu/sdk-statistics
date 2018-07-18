package com.yishen.sdk.shiro.session;

import com.yishen.sdk.cache.ICacheClient;
import com.yishen.sdk.utils.SerializableUtil;
import org.apache.commons.lang.ObjectUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.ValidatingSession;
import org.apache.shiro.session.mgt.eis.CachingSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.*;

import static com.yishen.sdk.enums.PropKeyEnum.PROP_TYPE;
import static com.yishen.sdk.enums.UpmsCacheKeyEnum.*;


/**
 * 基于redis的sessionDao，缓存共享session
 * Created by shuzheng on 2017/2/23.
 */
@Component
public class UpmsSessionDao extends CachingSessionDAO {

	protected static final org.apache.logging.log4j.Logger _log                         = LogManager.getLogger(UpmsSessionDao.class);
	// 会话key
	private final static   String                          LMHD_UPMS_SHIRO_SESSION_ID   = CACHE_SHIRO_SESSION_ID.getKey();
	// 全局会话key
	private final static   String                          LMHD_UPMS_SERVER_SESSION_ID  = CACHE_SERVER_SESSION_ID.getKey();
	// 全局会话列表key
	private final static   String                          LMHD_UPMS_SERVER_SESSION_IDS = CACHE_SERVER_SESSION_IDS.getKey();
	// code key
	private final static   String                          LMHD_UPMS_SERVER_CODE        = CACHE_SERVER_CODE.getKey();
	// 局部会话key
	private final static   String                          LMHD_UPMS_CLIENT_SESSION_ID  = CACHE_CLIENT_SESSION_ID.getKey();
	// 单点同一个code所有局部会话key
	private final static   String                          LMHD_UPMS_CLIENT_SESSION_IDS = CACHE_CLIENT_SESSION_IDS.getKey();
	@Autowired
	private ICacheClient cacheClient;

	@Override
	protected Serializable doCreate(Session session) {
		_log.debug("UpmsSessionDao.doCreate进入");
		Long start = System.currentTimeMillis();
		Serializable sessionId = generateSessionId(session);
		assignSessionId(session, sessionId);
		cacheClient.set(LMHD_UPMS_SHIRO_SESSION_ID + "_" + sessionId, (int) session.getTimeout() / 1000, SerializableUtil.serialize(session));
		_log.debug("doCreate >>>>> sessionId={}", sessionId);
		Long end = System.currentTimeMillis();
		_log.debug("UpmsSessionDao.doCreate结束，耗时：" + (end - start));
		return sessionId;
	}

	@Override
	protected Session doReadSession(Serializable sessionId) {
		_log.debug("UpmsSessionDao.doReadSession进入");
		Long start = System.currentTimeMillis();
		String session = cacheClient.get(LMHD_UPMS_SHIRO_SESSION_ID + "_" + sessionId);
		_log.debug("doReadSession >>>>> sessionId={}", sessionId);
		Long end = System.currentTimeMillis();
		_log.debug("UpmsSessionDao.doReadSession结束，耗时：" + (end - start));
		return SerializableUtil.deserialize(session);
	}

	@Override
	protected void doUpdate(Session session) {
		_log.debug("UpmsSessionDao.doUpdate进入");
		Long start = System.currentTimeMillis();
		// 如果会话过期/停止 没必要再更新了
		if (session instanceof ValidatingSession && !((ValidatingSession) session).isValid()) {
			Long end = System.currentTimeMillis();
			_log.info("UpmsSessionDao.doUpdate结束，耗时：" + (end - start));
			return;
		}
		// 更新session的最后一次访问时间
		UpmsSession upmsSession = (UpmsSession) session;
		UpmsSession cacheUpmsSession = (UpmsSession) doReadSession(session.getId());
		if (null != cacheUpmsSession) {
			upmsSession.setStatus(cacheUpmsSession.getStatus());
			upmsSession.setAttribute("FORCE_LOGOUT", cacheUpmsSession.getAttribute("FORCE_LOGOUT"));
		}
		//_log.info("###########更新session有效期");
		cacheClient.set(LMHD_UPMS_SHIRO_SESSION_ID + "_" + session.getId(), (int) session.getTimeout() / 1000, SerializableUtil.serialize(session));
		// 更新LMHD_UPMS_SERVER_SESSION_ID、LMHD_UPMS_SERVER_CODE过期时间 TODO
		_log.debug("doUpdate >>>>> sessionId={}", session.getId());
		Long end = System.currentTimeMillis();
		_log.debug("UpmsSessionDao.doUpdate结束，耗时：" + (end - start));
	}

	@Override
	protected void doDelete(Session session) {
		_log.debug("UpmsSessionDao.doDelete进入");
		Long start = System.currentTimeMillis();
		String sessionId = session.getId().toString();
		String upmsType = ObjectUtils.toString(session.getAttribute(PROP_TYPE.getKey()));
		//		String upmsType = "server";
		if ("client".equals(upmsType)) {
			// 删除局部会话和同一code注册的局部会话
			String code = cacheClient.get(LMHD_UPMS_CLIENT_SESSION_ID + "_" + sessionId);
			cacheClient.delete(LMHD_UPMS_CLIENT_SESSION_ID + "_" + sessionId);
			cacheClient.srem(LMHD_UPMS_CLIENT_SESSION_IDS + "_" + code, sessionId);
		}
		if ("server".equals(upmsType)) {
			// 当前全局会话code
			String code = cacheClient.get(LMHD_UPMS_SERVER_SESSION_ID + "_" + sessionId);
			// 清除全局会话
			cacheClient.delete(LMHD_UPMS_SERVER_SESSION_ID + "_" + sessionId);
			// 清除code校验值
			cacheClient.delete(LMHD_UPMS_SERVER_CODE + "_" + code);
			// 清除所有局部会话
			Set<String> clientSessionIds = cacheClient.smembers(LMHD_UPMS_CLIENT_SESSION_IDS + "_" + code);
			for (String clientSessionId : clientSessionIds) {
				cacheClient.delete(LMHD_UPMS_CLIENT_SESSION_ID + "_" + clientSessionId);
				cacheClient.srem(LMHD_UPMS_CLIENT_SESSION_IDS + "_" + code, clientSessionId);
			}
			_log.debug("当前code={}，对应的注册系统个数：{}个", code, cacheClient.scard(LMHD_UPMS_CLIENT_SESSION_IDS + "_" + code));
			// 维护会话id列表，提供会话分页管理
			cacheClient.lrem(LMHD_UPMS_SERVER_SESSION_IDS, 1, sessionId);
		}
		// 删除session
		cacheClient.delete(LMHD_UPMS_SHIRO_SESSION_ID + "_" + sessionId);
		_log.debug("doUpdate >>>>> sessionId={}", sessionId);
		Long end = System.currentTimeMillis();
		_log.debug("UpmsSessionDao.doDelete结束，耗时：" + (end - start));
	}

	/**
	 * 获取会话列表
	 *
	 * @param offset
	 * @param limit
	 * @return
	 */
	public Map getActiveSessions(int offset, int limit) {
		_log.debug("UpmsSessionDao.getActiveSessions进入");
		Long start = System.currentTimeMillis();
		Map sessions = new HashMap();
		// 获取在线会话总数
		long total = cacheClient.llen(LMHD_UPMS_SERVER_SESSION_IDS);
		// 获取当前页会话详情
		List<String> ids = cacheClient.lrange(LMHD_UPMS_SERVER_SESSION_IDS, offset, (offset + limit - 1));
		List<Session> rows = new ArrayList<>();
		for (String id : ids) {
			String session = cacheClient.get(LMHD_UPMS_SHIRO_SESSION_ID + "_" + id);
			// 过滤redis过期session
			if (null == session) {
				cacheClient.lrem(LMHD_UPMS_SERVER_SESSION_IDS, 1, id);
				total = total - 1;
				continue;
			}
			rows.add(SerializableUtil.deserialize(session));
		}
		sessions.put("total", total);
		sessions.put("rows", rows);
		Long end = System.currentTimeMillis();
		_log.debug("UpmsSessionDao.getActiveSessions结束，耗时：" + (end - start));
		return sessions;
	}

	/**
	 * 取得所有有效的 session.
	 *
	 * @return
	 */
	@Override
	public Collection<Session> getActiveSessions() {
		_log.debug("=> Get active sessions");
		Set<String> keys = cacheClient.keySet(LMHD_UPMS_SHIRO_SESSION_ID + "*");//TODO 看是否用到
		List<Session> sessions = new LinkedList<Session>();
		for (String key : keys) {
			String sessionInfo = cacheClient.get(key);
			sessions.add(SerializableUtil.deserialize(sessionInfo));
		}
		return sessions;
	}

	/**
	 * 强制退出
	 *
	 * @param ids
	 * @return
	 */
	public int forceout(String ids) {
		_log.debug("UpmsSessionDao.forceout进入");
		Long start = System.currentTimeMillis();
		String[] sessionIds = ids.split(",");
		for (String sessionId : sessionIds) {
			// 会话增加强制退出属性标识，当此会话访问系统时，判断有该标识，则退出登录
			String session = cacheClient.get(LMHD_UPMS_SHIRO_SESSION_ID + "_" + sessionId);
			UpmsSession upmsSession = (UpmsSession) SerializableUtil.deserialize(session);
			upmsSession.setStatus(UpmsSession.OnlineStatus.force_logout);
			upmsSession.setAttribute("FORCE_LOGOUT", "FORCE_LOGOUT");
			cacheClient.set(LMHD_UPMS_SHIRO_SESSION_ID + "_" + sessionId, (int) upmsSession.getTimeout() / 1000, SerializableUtil.serialize(upmsSession));
		}
		Long end = System.currentTimeMillis();
		_log.debug("UpmsSessionDao.forceout结束，耗时：" + (end - start));
		return sessionIds.length;
	}

	/**
	 * 更改在线状态
	 *
	 * @param sessionId
	 * @param onlineStatus
	 */
	public void updateStatus(Serializable sessionId, UpmsSession.OnlineStatus onlineStatus) {
		_log.debug("UpmsSessionDao.updateStatus进入");
		Long start = System.currentTimeMillis();
		UpmsSession session = (UpmsSession) doReadSession(sessionId);
		if (null == session) {
			Long end = System.currentTimeMillis();
			_log.info("UpmsSessionDao.updateStatus结束，耗时：" + (end - start));
			return;
		}
		session.setStatus(onlineStatus);
		cacheClient.set(LMHD_UPMS_SHIRO_SESSION_ID + "_" + session.getId(), (int) session.getTimeout() / 1000, SerializableUtil.serialize(session));
		Long end = System.currentTimeMillis();
		_log.debug("UpmsSessionDao.updateStatus结束，耗时：" + (end - start));
	}
}
