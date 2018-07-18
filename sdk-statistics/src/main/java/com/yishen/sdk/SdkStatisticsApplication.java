package com.yishen.sdk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ServletComponentScan(basePackages = "com.yishen.sdk.controller.*")
@ComponentScan( basePackages = "com.yishen.sdk.*")
public class SdkStatisticsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SdkStatisticsApplication.class, args);
	}
}
