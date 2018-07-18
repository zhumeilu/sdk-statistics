package com.yishen.sdk.shiro.listener;

import org.apache.logging.log4j.LogManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;
import org.springframework.stereotype.Component;

/**
 * Created by shuzheng on 2017/2/12.
 */
@Component
public class UpmsSessionListener implements SessionListener {

	protected static final org.apache.logging.log4j.Logger _log = LogManager.getLogger(UpmsSessionListener.class);

	@Override
	public void onStart(Session session) {
		_log.debug("会话创建：" + session.getId());
	}

	@Override
	public void onStop(Session session) {
		_log.debug("会话停止：" + session.getId());
	}

	@Override
	public void onExpiration(Session session) {
		_log.debug("会话过期：" + session.getId());
	}
}
