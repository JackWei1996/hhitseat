
 /*
 * All rights Reserved, Copyright (C) Aisino LIMITED 2019
 * FileName: LoginVerify.java
 * Version:  1.0
 * Modify record:
 * NO. |     Date       |    Name        |      Content
 * 1   | 2019年3月27日        | Aisino)Jack    | original version
 */
package com.jack.hhitseat.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.jack.hhitseat.task.MyRunnable;

/**
 * class name: LoginVerify <BR>
 * class description: please write your description <BR>
 * @version 1.0  2019年3月27日 下午4:39:10
 * @author Aisino)chenwei
 */
public class LoginVerify {
	
	private static final Logger logger = LoggerFactory.getLogger(LoginVerify.class);
	
	static String body = null;
	static String VIEWSTATE = "/wEPDwUJNjU0ODExMzM0ZGQfKz+nYo4XQMFAENFjh2BOCkXk0fYoklhR6Vtc9kqiLg==";
	static String EVENTVALIDATION = "/wEWBAKC8vr8DQL36ofzAQKLh62dDwLE8+zdCBkC9Dgcs1P2tigAuqNqVFJg2BdHUxxBJiq+6hpiM5CU";
	
	public static String getBody() {
		HttpHeaders headers = new HttpHeaders();
		RestTemplate template = new RestTemplate();
		
		String url = "http://seat.hhit.edu.cn/pages/ic/LoginForm.aspx";
		
		HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<MultiValueMap<String, Object>>(null,headers);
		ResponseEntity<String> response2 = template.postForEntity(url, httpEntity, String.class);
		
		logger.warn("获取登录页面Body");

		return response2.getBody();
	}
	
	public static String getVIEWSTATE() {
		if(body==null) {			
			body = getBody();
		}
		if(body!=null) {
			String itm = body;
			body = null;
			return itm.split("id=\"__VIEWSTATE\" value=\"")[1].split("\" />")[0];
		}
		body = null;
		return VIEWSTATE;
	}
	
	public static String getEVENTVALIDATION() {
		if(body==null) {			
			body = getBody();
		}
		if(body!=null) {	
			String itm = body;
			body = null;
			return itm.split("id=\"__EVENTVALIDATION\" value=\"")[1].split("\" />")[0];
		}
		body = null;
		return EVENTVALIDATION;
	}
}
