package com.jack.hhitseat;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import springfox.documentation.swagger2.annotations.EnableSwagger2;
@SpringBootApplication
@EnableScheduling//启动定时任务
@EnableAutoConfiguration
@MapperScan("com.jack.hhitseat.mapper.*")
@EnableSwagger2
public class HhitseatApplication{

	public static void main(String[] args) {
		SpringApplication.run(HhitseatApplication.class, args);
	}
}
