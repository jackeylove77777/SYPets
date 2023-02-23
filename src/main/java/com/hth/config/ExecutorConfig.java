package com.hth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 自定义线程池
 */
@Configuration
@EnableAsync
public class ExecutorConfig {
 
	//核心线程数
	private int corePoolSize = Runtime.getRuntime().availableProcessors()/2;
	//最大线程数，当空闲线程数大于核心线程数时，将会回收多余的空闲线程数
	private int maxPoolSize = Runtime.getRuntime().availableProcessors();
	//线程阻塞队列的大小
	private int queueCapacity = 10;
	@Bean
	public Executor mySimpleAsync() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(corePoolSize);
		executor.setMaxPoolSize(maxPoolSize);
		executor.setQueueCapacity(queueCapacity);
		executor.setThreadNamePrefix("mySimpleExecutor-");
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		executor.initialize();
		return executor;
	}
}
