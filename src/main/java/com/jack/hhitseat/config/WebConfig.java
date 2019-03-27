/*
 * All rights Reserved, Copyright (C) Aisino LIMITED 2018
 * FileName: WebConfig.java
 * Version:  $Revision$
 * Modify record:
 * NO. |     Date       |    Name         |      Content
 * 1   | 2019年1月2日        | Aisino)Jack    | original version
 */
package com.jack.hhitseat.config;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * class name:WebConfig <BR>
 * class description: 全局配置文件 <BR>
 * Remark: <BR>
 * 
 * @version 1.00 2019年1月2日
 * @author Aisino)weihaohao
 */
@Configuration
public class WebConfig {

	@Autowired
	private Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder;

	@Bean
	public MappingJackson2HttpMessageConverter MappingJsonpHttpMessageConverter() {

		ObjectMapper mapper = jackson2ObjectMapperBuilder.build();

		MappingJackson2HttpMessageConverter mappingJsonpHttpMessageConverter = new MappingJackson2HttpMessageConverter(
				mapper);
		return mappingJsonpHttpMessageConverter;
	}
}
