
 /*
 * All rights Reserved, Copyright (C) Aisino LIMITED 2019
 * FileName: LoginVerify.java
 * Version:  1.0
 * Modify record:
 * NO. |     Date       |    Name        |      Content
 * 1   | 2019年3月27日        | Aisino)Jack    | original version
 */
package com.jack.hhitseat.utils;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * class name: LoginVerify <BR>
 * class description: please write your description <BR>
 * @version 1.0  2019年3月27日 下午4:39:10
 * @author Aisino)weihaohao
 */
public class LoginVerify {
	
	static String body = null;
	static String VIEWSTATE = "/wEPDwUJNjU0ODExMzM0ZGRgUvbey+950Xh7OEw/3GjPUybDryxCZIHQ5K66jJE7yw==";
	static String EVENTVALIDATION = "/wEWBALjq7r5AQL36ofzAQKLh62dDwLE8+zdCFZDURdcT2Kt0SYlY4v4gQpOiMa+muRIorC4AKL3mnAV";
	
	public static String getBody() {
		HttpHeaders headers = new HttpHeaders();
		RestTemplate template = new RestTemplate();
		
		String url = "http://seat.hhit.edu.cn/pages/ic/LoginForm.aspx";
		
		HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<MultiValueMap<String, Object>>(null,headers);
		ResponseEntity<String> response2 = template.postForEntity(url, httpEntity, String.class);
		
		String body = response2.getBody();

		return body;
	}
	
	public static String getVIEWSTATE() {
		if(body==null) {			
			body = getBody();
		}
		if(body!=null) {			
			return body.split("id=\"__VIEWSTATE\" value=\"")[1].split("\" />")[0];
		}
		return VIEWSTATE;
	}
	
	public static String getEVENTVALIDATION() {
		if(body==null) {			
			body = getBody();
		}
		if(body!=null) {			
			return body.split("id=\"__EVENTVALIDATION\" value=\"")[1].split("\" />")[0];
		}
		return EVENTVALIDATION;
	}
}
