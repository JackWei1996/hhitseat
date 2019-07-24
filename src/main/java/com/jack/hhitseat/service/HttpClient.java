/**
 * 
 */
package com.jack.hhitseat.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class HttpClient {
	private final Logger logger = LoggerFactory.getLogger(HttpClient.class);
	private static HttpHeaders header = new HttpHeaders();
	private final static String YZM_URL = "http://seat.hhit.edu.cn/ClientWeb/pro/page/image.aspx";
	
	public HttpClient() {
		header.set("Accept","application/json, text/javascript, */*; q=0.01");
		header.set("Accept-Encoding","gzip, deflate");
		header.set("Accept-Language","zh-CN,zh;q=0.9");
		header.set("Connection","keep-alive");
		header.set("Host","seat.hhit.edu.cn");
		header.set("Referer","http://seat.hhit.edu.cn/ClientWeb/xcus/ic2/Default.aspx");
		header.set("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36");
		header.set("X-Requested-With","XMLHttpRequest");
	}
	 /**
	 * Method name: client <BR>
	 * Description: 登录获取session <BR>
	 * @param url
	 * @param method
	 * @param params
	 * @return  String<BR>
	 */
	public String client(String url, HttpMethod method, MultiValueMap<String, Object> params) {
		
		HttpHeaders headers = new HttpHeaders();
		RestTemplate template = new RestTemplate();
		HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<MultiValueMap<String, Object>>(params,headers);
		ResponseEntity<String> response2 = template.postForEntity(url, httpEntity, String.class);
		
		String body = response2.getBody();

		String session ="LOGIN_ERR";//登录错误
		String result = null;
		try {
			result = body.split("<span id=\"MSG\">")[1].split("</span></div></td></tr>")[0];
			if(result.contains("无管理权限")) {			
				session =response2.getHeaders().toString().split("ASP.NET_SessionId=")[1].split("; path=/; ")[0];
			}
		}catch (Exception e) {
			session =response2.getHeaders().toString().split("ASP.NET_SessionId=")[1].split("; path=/; ")[0];
		}

		return session;
	}
	
	public String login(String url, String session) {
		RestTemplate template = new RestTemplate();
		header.add("Cookie","ASP.NET_SessionId="+session);
		HttpEntity<String> entity = new HttpEntity<>(header);
		
		ResponseEntity<String> response = template.exchange(url, HttpMethod.GET, entity, String.class);

		return response.getBody();
	}
	
	public String qz(String url, String session, MultiValueMap<String, Object> params) {
		RestTemplate template = new RestTemplate();

		header.set("Cookie","ASP.NET_SessionId="+session);

		HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<MultiValueMap<String, Object>>(header);
		
		ResponseEntity<String> response = template.exchange(url, HttpMethod.GET, httpEntity, String.class,params);

		return response.getBody();
	}
	
	/**
	 * 请求验证码
	 * @param session
	 */
	public void yzm(String session) {
		RestTemplate template = new RestTemplate();
		header.set("Cookie","ASP.NET_SessionId="+session);
		HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<MultiValueMap<String, Object>>(null,header);

		ResponseEntity<String> response = null;
		try {
			response = template.exchange(YZM_URL, HttpMethod.GET, httpEntity, String.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String qz2(String url, String session, Map<String, Object> params) {
		RestTemplate template = new RestTemplate();
		header.set("Cookie","ASP.NET_SessionId="+session);
		HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<MultiValueMap<String, Object>>(null,header);

		ResponseEntity<String> response = null;
		
		try {
			response = template.exchange(url, HttpMethod.GET, httpEntity, String.class, params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return response.getBody();
	}
}
