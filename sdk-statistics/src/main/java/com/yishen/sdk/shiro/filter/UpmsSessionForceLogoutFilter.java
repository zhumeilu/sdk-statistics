package com.yishen.sdk.shiro.filter;

import org.apache.logging.log4j.LogManager;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 强制退出会话过滤器
 * Created by shuzheng on 2017/3/1.
 */
public class UpmsSessionForceLogoutFilter extends AccessControlFilter {

	protected static final org.apache.logging.log4j.Logger _log = LogManager.getLogger(UpmsSessionForceLogoutFilter.class);

	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
		_log.debug("UpmsSessionForceLogoutFilter.isAccessAllowed进入");
		Long start = System.currentTimeMillis();
		Subject subject = SecurityUtils.getSubject();
		subject.getPrincipal();
		Session session = getSubject(request, response).getSession(false);
		if (session == null) {
			return true;
		}
		boolean forceout = session.getAttribute("FORCE_LOGOUT") == null;
		Long end = System.currentTimeMillis();
		_log.debug("UpmsSessionForceLogoutFilter.isAccessAllowed结束，耗时：" + (end - start));
		return forceout;
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		_log.debug("UpmsSessionForceLogoutFilter.onAccessDenied进入");
		Long start = System.currentTimeMillis();
		getSubject(request, response).logout();
		String loginUrl = getLoginUrl() + (getLoginUrl().contains("?") ? "&" : "?") + "forceLogout=1";
		WebUtils.issueRedirect(request, response, loginUrl);
		Long end = System.currentTimeMillis();
		_log.debug("UpmsSessionForceLogoutFilter.onAccessDenied结束，耗时：" + (end - start));
		return false;
	}
}
