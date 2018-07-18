package com.yishen.sdk.shiro.realm;

import com.yishen.sdk.cache.ICacheClient;
import com.yishen.sdk.manager.IPermissionManager;
import com.yishen.sdk.manager.IRoleManager;
import com.yishen.sdk.manager.IUserManager;
import com.yishen.sdk.model.Permission;
import com.yishen.sdk.model.Role;
import com.yishen.sdk.model.User;
import com.yishen.sdk.shiro.session.UpmsSessionDao;
import com.yishen.sdk.utils.CoreCodecUtils;
import com.yishen.sdk.utils.PropertiesFileUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.yishen.sdk.enums.PropKeyEnum.PROP_TYPE;
import static com.yishen.sdk.enums.UpmsCacheKeyEnum.CACHE_SERVER_SESSION_ID;

/**
 * 用户认证和授权
 * Created by shulmhd- on 2017/1/20.
 */
public class UpmsRealm extends AuthorizingRealm {

	// 全局会话key
	private final static   String                          LMHD_UPMS_SERVER_SESSION_ID = CACHE_SERVER_SESSION_ID.getKey();
	protected static final org.apache.logging.log4j.Logger _log                        = LogManager.getLogger(UpmsRealm.class);
	@Autowired
	private IUserManager userManager;
	@Autowired
	private IRoleManager roleManager;
	@Autowired
	private IPermissionManager permissionManager;
	@Autowired
	private UpmsSessionDao upmsSessionDao;
	@Autowired
	private ICacheClient cacheClient;


	/**
	 * 授权：验证权限时调用
	 *
	 * @param principalCollection
	 * @return
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
		_log.debug("UpmsRealm.doGetAuthorizationInfo进入");
		Long start = System.currentTimeMillis();
		String username = (String) principalCollection.getPrimaryPrincipal();
		User user = userManager.selectUserByUsername(username);
		// 当前用户所有角色
		List<Role> upmsRoles = roleManager.selectRoleByUserId(user.getId());
		Set<String> roles = upmsRoles.stream().filter(e->StringUtils.isNotBlank(e.getName())).map(Role::getName).collect(Collectors.toSet());
		// 当前用户所有权限
		List<Long> roleIds = new ArrayList<>();
		upmsRoles.forEach(role->{
			roleIds.add(role.getId());
		});
		List<Permission> upmsPermissions = permissionManager.selectByRoleIds(roleIds);
		Set<String> permissions = upmsPermissions.stream().filter(e -> StringUtils.isNotBlank(e.getPermissionValue())).map(Permission::getPermissionValue).collect(Collectors.toSet());
		SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
		simpleAuthorizationInfo.setStringPermissions(permissions);
		simpleAuthorizationInfo.setRoles(roles);
		Long end = System.currentTimeMillis();
		_log.debug("UpmsRealm.doGetAuthorizationInfo结束，耗时：" + (end - start));
		return simpleAuthorizationInfo;
	}

	/**
	 * 认证：登录时调用
	 *
	 * @param authenticationToken
	 * @return
	 * @throws AuthenticationException
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
		_log.debug("UpmsRealm.doGetAuthenticationInfo进入");
		Long start = System.currentTimeMillis();
		String username = (String) authenticationToken.getPrincipal();
		String password = new String((char[]) authenticationToken.getCredentials());
		// client无密认证
		String upmsType = PropertiesFileUtil.getInstance().get(PROP_TYPE.getKey());
		if ("client".equals(upmsType)) {
			return new SimpleAuthenticationInfo(username, password, getName());
		}
		Set<String> keys = cacheClient.keySet(LMHD_UPMS_SERVER_SESSION_ID + "_*");
		for (String key : keys) {
			Session se = upmsSessionDao.readSession(key.split(LMHD_UPMS_SERVER_SESSION_ID + "_")[1]);
			if (username.equals(String.valueOf(se.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY)))) {
				upmsSessionDao.forceout(se.getId().toString());
			}
		}
		// 查询用户信息
		User upmsUser = userManager.selectUserByUsername(username);
		if (null == upmsUser) {
			throw new UnknownAccountException();
		}
		if (!upmsUser.getPassword().equalsIgnoreCase(CoreCodecUtils.encryptMD5((password + upmsUser.getSalt())))) {
			throw new IncorrectCredentialsException();
		}
		if (upmsUser.getLocked() == 1) {
			throw new LockedAccountException();
		}
		Long end = System.currentTimeMillis();
		_log.debug("UpmsRealm.doGetAuthenticationInfo结束，耗时：" + (end - start));
		return new SimpleAuthenticationInfo(username, password, getName());
	}
}
