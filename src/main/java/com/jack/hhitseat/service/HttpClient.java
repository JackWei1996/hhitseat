/**
 * 
 */
package com.jack.hhitseat.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * @author 19604
 *
 */
@Service
public class HttpClient {
	
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
		
	//System.out.println(body);
		
		String session ="LOGIN_ERR";//登录错误
		String result = body.split("<span id=\"MSG\">")[1].split("</span></div></td></tr>")[0];
		//System.out.println(result);
		if(result.contains("无管理权限")) {			
			session =response2.getHeaders().toString().split("ASP.NET_SessionId=")[1].split("; path=/; ")[0];
		}

		return session;
	}
	
	public String login(String url, String session) {
		RestTemplate template = new RestTemplate();
		HttpHeaders header = new HttpHeaders();
		
		header.add("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		header.add("Accept-Encoding","gzip, deflate");
		header.add("Accept-Language","zh-CN,zh;q=0.9");
		header.add("Connection","keep-alive");
		header.add("Cookie","ASP.NET_SessionId="+session);
		header.add("Host","seat.hhit.edu.cn");
		header.add("Referer","http://seat.hhit.edu.cn/pages/ic/LoginForm.aspx");
		header.add("Upgrade-Insecure-Requests","1");
		header.add("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36");

		HttpEntity<String> entity = new HttpEntity<>(header);
		
		ResponseEntity<String> response = template.exchange(url, HttpMethod.GET, entity, String.class);

		return response.getBody();
	}
}
