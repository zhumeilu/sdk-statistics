package com.yishen.sdk.config;

import com.yishen.sdk.cache.RedisClient;
import com.yishen.sdk.utils.PropertiesFileUtil;
import com.yishen.sdk.shiro.cache.RedisCacheManager;
import com.yishen.sdk.shiro.filter.UpmsAuthenticationFilter;
import com.yishen.sdk.shiro.filter.UpmsSessionForceLogoutFilter;
import com.yishen.sdk.shiro.listener.UpmsSessionListener;
import com.yishen.sdk.shiro.realm.UpmsRealm;
import com.yishen.sdk.shiro.session.UpmsSessionDao;
import com.yishen.sdk.shiro.session.UpmsSessionFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.PropertySource;

import javax.annotation.PostConstruct;
import javax.servlet.Filter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.yishen.sdk.enums.PropKeyEnum.*;

/**
 * 功能：
 * Charles on 2017/10/10.
 */
@PropertySource("classpath:application.properties")
@Configuration
public class ShiroConfiguration {

	//<!-- Shiro生命周期处理器-->
	@Bean(name = "lifecycleBeanPostProcessor")
	public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
		return new LifecycleBeanPostProcessor();
	}

	//<!-- 开启Shiro Spring AOP权限注解@RequiresPermissions的支持 -->
	@Bean
	@ConditionalOnMissingBean
	@DependsOn("lifecycleBeanPostProcessor")
	public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
		DefaultAdvisorAutoProxyCreator daap = new DefaultAdvisorAutoProxyCreator();
		daap.setProxyTargetClass(true);
		return daap;
	}

	//
	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
		AuthorizationAttributeSourceAdvisor aasa = new AuthorizationAttributeSourceAdvisor();
		aasa.setSecurityManager(securityManager());
		return aasa;
	}

	//	//redis缓存
	@Bean("cacheClient")
	@ConfigurationProperties(prefix = "redis")
	public RedisClient cacheClient() {
		return new RedisClient();
	}
	//
	//<!-- 强制退出会话过滤器 -->
	//	@Bean(name = "upmsSessionForceLogout")
	//	public UpmsSessionForceLogoutFilter upmsSessionForceLogout() {
	//		return new UpmsSessionForceLogoutFilter();
	//	}
	//<!-- 重写authc过滤器 -->
	//	@Bean(name = "upmsAuthenticationFilter")
	//	public UpmsAuthenticationFilter upmsAuthenticationFilter() {
	//		return new UpmsAuthenticationFilter();
	//	}

	//<!-- 自定义cacheManager -->
	@Bean
	public RedisCacheManager cacheManager() {
		RedisCacheManager rcm = new RedisCacheManager();
		rcm.setRedisManager(cacheClient());
		return rcm;
	}

	//<!-- 会话监听器 -->
	@Bean
	public UpmsSessionListener sessionListener() {
		return new UpmsSessionListener();
	}

	//
	//<!-- session工厂 -->
	@Bean("sessionFactory")
	public UpmsSessionFactory sessionFactory() {
		return new UpmsSessionFactory();
	}

	//
	//	//<!-- 会话Cookie模板 -->
	@Bean("sessionIdCookie")
	public SimpleCookie sessionIdCookie() {
		SimpleCookie sc = new SimpleCookie();
		//<!-- 不会暴露给客户端 -->
		sc.setHttpOnly(true);
		//<!-- 设置Cookie的过期时间，秒为单位，默认-1表示关闭浏览器时过期Cookie -->
		sc.setMaxAge(-1);
		//<!-- Cookie名称 -->
		String name = PropertiesFileUtil.getInstance().get(PROP_SESSION_ID.getKey());
		sc.setName(name);
		return sc;
	}

	//
	//<!-- 会话DAO，可重写，持久化session -->
	@Bean("sessionDAO")
	public UpmsSessionDao sessionDAO() {
		return new UpmsSessionDao();
	}

	@Bean
	public DefaultWebSessionManager sessionManager() {
		Long timeout = PropertiesFileUtil.getInstance().getLong(PROP_SESSION_TIMEOUT.getKey());
		DefaultWebSessionManager dwm = new DefaultWebSessionManager();
		dwm.setGlobalSessionTimeout(timeout);
		dwm.setSessionDAO(sessionDAO());
		dwm.setSessionIdCookieEnabled(true);
		dwm.setSessionIdCookie(sessionIdCookie());
		dwm.setSessionIdUrlRewritingEnabled(false);
		dwm.setSessionValidationSchedulerEnabled(false);
		dwm.setSessionListeners(Arrays.asList(sessionListener()));
		dwm.setSessionFactory(sessionFactory());
		return dwm;
	}

	//
	//	// <!-- rememberMe缓存cookie -->
	@Bean
	public SimpleCookie rememberMeCookie() {
		SimpleCookie sc = new SimpleCookie("rememberMe");
		//<!-- 不会暴露给客户端 -->
		sc.setHttpOnly(true);
		//<!-- 记住我cookie生效时间 -->
		Integer maxAge = PropertiesFileUtil.getInstance().getInt(PROP_REMEMBERME_TIMEOUT.getKey());
		sc.setMaxAge(maxAge);
		return sc;
	}

	//
	//	//<!-- rememberMe管理器 -->
	@Bean
	public CookieRememberMeManager rememberMeManager() {
		CookieRememberMeManager crm = new CookieRememberMeManager();
		crm.setCipherKey(org.apache.shiro.codec.Base64.decode("4AvVhmFLUs0KTA3Kprsdag=="));
		crm.setCookie(rememberMeCookie());
		return crm;
	}

	@Bean
	public UpmsRealm upmsRealm() {
		UpmsRealm upmsRealm = new UpmsRealm();
		upmsRealm.setCacheManager(cacheManager());
		return upmsRealm;
	}

	//<!-- 安全管理器 -->
	@Bean
	public DefaultWebSecurityManager securityManager() {
		DefaultWebSecurityManager dwm = new DefaultWebSecurityManager();
		dwm.setRealm(upmsRealm());
		dwm.setCacheManager(cacheManager());
		dwm.setSessionManager(sessionManager());
		dwm.setRememberMeManager(rememberMeManager());
		return dwm;
	}

	/**
	 * shiro过滤器
	 * @return
	 */
	@Bean
	public ShiroFilterFactoryBean shiroFilter() {
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		shiroFilterFactoryBean.setSecurityManager(securityManager());
		String loginUrl = PropertiesFileUtil.getInstance().get(PROP_SSO_SERVER_URL.getKey());
		shiroFilterFactoryBean.setLoginUrl(loginUrl);
		String successUrl = PropertiesFileUtil.getInstance().get(PROP_SSO_SUCCESS_URL.getKey());
		shiroFilterFactoryBean.setSuccessUrl(successUrl);
		shiroFilterFactoryBean.setUnauthorizedUrl("/403");
		Map<String,Filter> mf = new HashMap<>();
		mf.put("myauthc", upmsAuthenticationFilter());
		mf.put("mylogout", upmsSessionForceLogoutFilter());
		shiroFilterFactoryBean.setFilters(mf);
		StringBuilder sb = new StringBuilder();
		sb.append("/webjars/** = anon").append("\n");
		sb.append("/uistatic/** = anon").append("\n");
//		sb.append("/sso/login = anon").append("\n");
		sb.append("/manage/** = myauthc,mylogout").append("\n");
		sb.append("/manage/index = user").append("\n");
		sb.append("/logout = logout").append("\n");
		sb.append("/** = anon");
		shiroFilterFactoryBean.setFilterChainDefinitions(sb.toString());
		return shiroFilterFactoryBean;
	}

	@Bean
	@DependsOn("cacheClient")
	public UpmsAuthenticationFilter upmsAuthenticationFilter() {
		return new UpmsAuthenticationFilter();
	}

	@Bean
	public UpmsSessionForceLogoutFilter upmsSessionForceLogoutFilter() {
		return new UpmsSessionForceLogoutFilter();
	}

	@PostConstruct
	public void postConstruct() {
		SecurityUtils.setSecurityManager(securityManager());
	}
}
