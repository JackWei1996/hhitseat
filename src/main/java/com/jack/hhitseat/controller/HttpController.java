/**
 * 
 */
package com.jack.hhitseat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jack.hhitseat.service.HttpClient;

/**
 * @author 19604
 *
 */
@RestController
@RequestMapping(value="/ht")
public class HttpController {
	@Autowired
	HttpClient httpClient;
	
	@RequestMapping(value="/ct")
	public String ct() {
		String url = "http://seat.hhit.edu.cn/pages/ic/LoginForm.aspx";
		HttpMethod method = HttpMethod.POST;
		MultiValueMap<String, Object> params = new LinkedMultiValueMap<String, Object>();
		
		params.add("__VIEWSTATE", "/wEPDwUJNjU0ODExMzM0ZGRgUvbey+950Xh7OEw/3GjPUybDryxCZIHQ5K66jJE7yw==");
		params.add("__EVENTVALIDATION", "/wEWBALjq7r5AQL36ofzAQKLh62dDwLE8+zdCFZDURdcT2Kt0SYlY4v4gQpOiMa+muRIorC4AKL3mnAV");
		params.add("szLogonName", "2015150240");
		params.add("szMiss", "2015150240");
		params.add("Button_Logon", "登录");
		
		String session = httpClient.client(url, method, params);
		
		//String session = s.split("ASP.NET_SessionId=")[1].split("; path=/; ")[0];
		
		//System.out.println(session);
		
		
		//params.add("ASP.NET_SessionId", session);
		
		//httpClient.client("http://seat.hhit.edu.cn/ClientWeb/xcus/ic2/Default.aspx", method, params);
		
		String loginUrl = "http://seat.hhit.edu.cn/ClientWeb/xcus/ic2/Default.aspx";
		
		String s = httpClient.login(loginUrl, session);
		//System.out.println(s);
		return s;
	}
}
