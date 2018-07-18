package com.yishen.sdk.config;

//import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
//import org.springframework.boot.web.servlet.ErrorPage;
import com.yishen.sdk.interceptor.LogAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 功能：
 * Charles on 2017/9/28.
 */
@Configuration
public class BeanConfiguration {

//	@Bean("lmhdAdminUtil")
//	public LmhdAdminUIUtil lmhdAdminUtil() {
//		return new LmhdAdminUIUtil();
//	}

	@Bean("threadPoolTaskExecutor")
	public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
		ThreadPoolTaskExecutor tpte = new ThreadPoolTaskExecutor();
		//线程池维护线程的最少数量
		tpte.setCorePoolSize(50);
		//线程池维护线程的最大数量，默认为Integer.MAX_VALUE
		tpte.setMaxPoolSize(1000);
		//线程池所使用的缓冲队列，一般需要设置值>=notifyScheduledMainExecutor.maxNum；默认为Integer.MAX_VALUE
		tpte.setQueueCapacity(20000);
		//线程池维护线程所允许的空闲时间，默认为60s
		tpte.setKeepAliveSeconds(300);
		//线程池对拒绝任务（无线程可用）的处理策略，目前只支持AbortPolicy、CallerRunsPolicy；默认为后者
		//<!-- AbortPolicy:直接抛出java.util.concurrent.RejectedExecutionException异常 -->
		//<!-- CallerRunsPolicy:主线程直接执行该任务，执行完之后尝试添加下一个任务到线程池中，可以有效降低向线程池内添加任务的速度 -->
		//<!-- DiscardOldestPolicy:抛弃旧的任务、暂不支持；会导致被丢弃的任务无法再次被执行 -->
		//<!-- DiscardPolicy:抛弃当前任务、暂不支持；会导致被丢弃的任务无法再次被执行 -->
		tpte.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		return tpte;
	}
//
//	/**
//	 * 错误页面处理类
//	 *
//	 * @return
//	 */
////	@Bean
////	public EmbeddedServletContainerCustomizer containerCustomizer() {
////		return (container -> {
////			ErrorPage error403Page = new ErrorPage(HttpStatus.FORBIDDEN, "/error/403.jsp");
////			ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/error/404.jsp");
////			ErrorPage error500Page = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error/500.jsp");
////			ErrorPage errorExceptionPage = new ErrorPage(Throwable.class, "/error/error.jsp");
////			container.addErrorPages(error403Page, error404Page, error500Page, errorExceptionPage);
////		});
////	}

    //<!-- 日志记录AOP实现 -->
    @Bean
    public LogAspect logAspect() {
        return new LogAspect();
    }
    //	@Bean
    //	public RpcLogAspect rpcLogAspect() {
    //		return new RpcLogAspect();
    //	}
}
