
 /*
 * All rights Reserved, Copyright (C) Aisino LIMITED 2019
 * FileName: IndexController.java
 * Version:  1.0
 * Modify record:
 * NO. |     Date       |    Name        |      Content
 * 1   | 2019年3月27日        | Aisino)Jack    | original version
 */
package com.jack.hhitseat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jack.hhitseat.model.ResultMap;
import com.jack.hhitseat.service.HttpClient;
import com.jack.hhitseat.utils.LoginVerify;

/**
 * class name: IndexController <BR>
 * class description: please write your description <BR>
 * @version 1.0  2019年3月27日 上午10:28:50
 * @author Aisino)weihaohao
 */
@Controller
public class IndexController {
	@Autowired
	HttpClient httpClient;
	
	private static String SESSION = "LOGIN_ERR";

	private static String VIEWSTATE = null;
	private static String EVENTVALIDATION = null;
	
	private ResultMap resultMap = new ResultMap();
	
	@RequestMapping(value="/index")
	public String index() {		
		return "zc";
	}
	
	@RequestMapping(value="/reg")
	@ResponseBody
	public Object regist(String u, String p,
			String zw1, String zw2, String zw3,
			String ip, String cname) {
		
		  String url = "http://seat.hhit.edu.cn/pages/ic/LoginForm.aspx"; 
		  HttpMethod method = HttpMethod.POST; 
		  MultiValueMap<String, Object> params = new LinkedMultiValueMap<String, Object>();
		  
		  if(VIEWSTATE==null || EVENTVALIDATION==null) {
			  VIEWSTATE = LoginVerify.getVIEWSTATE();
			  EVENTVALIDATION = LoginVerify.getEVENTVALIDATION();
		  }
		  
		  
		  params.add("__VIEWSTATE",VIEWSTATE);
		  params.add("__EVENTVALIDATION", EVENTVALIDATION); 
		  params.add("szLogonName", u); 
		  params.add("szMiss", p);
		  params.add("Button_Logon", "登录");
		  
		  SESSION = httpClient.client(url, method, params);
		 
		if(SESSION.equals("LOGIN_ERR")) {
			return resultMap.fail().message("用户名密码错误！");
		}
		
		System.out.println(LoginVerify.getVIEWSTATE());
		System.out.println(LoginVerify.getEVENTVALIDATION());
		
		/*
		 * System.out.println(SESSION);
		 * 
		 * String loginUrl = "http://seat.hhit.edu.cn/ClientWeb/xcus/ic2/Default.aspx";
		 * 
		 * String html = httpClient.login(loginUrl, SESSION);
		 * 
		 * System.out.println(html);
		 */
		
		return resultMap.success().message("注册成功");
	}
}
