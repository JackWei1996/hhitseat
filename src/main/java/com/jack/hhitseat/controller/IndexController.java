
 /*
 * All rights Reserved, Copyright (C) Aisino LIMITED 2019
 * FileName: IndexController.java
 * Version:  1.0
 * Modify record:
 * NO. |     Date       |    Name        |      Content
 * 1   | 2019年3月27日        | Aisino)Jack    | original version
 */
package com.jack.hhitseat.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jack.hhitseat.bean.User;
import com.jack.hhitseat.model.ResultMap;
import com.jack.hhitseat.service.HttpClient;
import com.jack.hhitseat.service.impl.UserServiceImpl;
import com.jack.hhitseat.utils.LoginVerify;
import com.jack.hhitseat.utils.MyUtils;

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
	@Autowired
	private UserServiceImpl userService;
	
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
	public Object regist(String u, String p,String em,
			String zw1, String zw2, String zw3,
			String ip, String cname) {
		
		if(!MyUtils.isEmail(em)) {
			return resultMap.fail().message("用户邮箱地址有问题！");
		}
		
		
		if(VIEWSTATE==null || EVENTVALIDATION==null) {
			  VIEWSTATE = LoginVerify.getVIEWSTATE();
			  EVENTVALIDATION = LoginVerify.getEVENTVALIDATION();
		}
		
		  String url = "http://seat.hhit.edu.cn/pages/ic/LoginForm.aspx"; 
		  HttpMethod method = HttpMethod.POST; 
		  MultiValueMap<String, Object> params = new LinkedMultiValueMap<String, Object>();

		  params.add("__VIEWSTATE",VIEWSTATE);
		  params.add("__EVENTVALIDATION", EVENTVALIDATION); 
		  params.add("szLogonName", u); 
		  params.add("szMiss", p);
		  params.add("Button_Logon", "登录");
		  
		SESSION = httpClient.client(url, method, params);
		
		if(SESSION.equals("LOGIN_ERR")) {
			return resultMap.fail().message("用户名密码错误！");
		}
			
		List<User> users = userService.getUserByNum(u);
		
		if(users.size()>0) {
			return resultMap.fail().message("该学号已经被注册！");
		}
		  
		  String loginUrl = "http://seat.hhit.edu.cn/ClientWeb/xcus/ic2/Default.aspx";
		  
		  String html = httpClient.login(loginUrl, SESSION);
		  
		  String name = html.split("acc.name = \"")[1].split("\";")[0];
		  
		  User user = new User();
		  
		  user.setUserName(name);
		  user.setEmail(em);
		  user.setIp(ip);
		  user.setStuNum(u);
		  user.setPassword(p);
		  user.setIsdo(1);
		  
		  String seat = "";
		  
		  if(!zw1.equals("-1=-----请选择座位-----")) {
			  seat+=zw1;
		  }
		  if(!zw2.equals("-1=-----请选择座位-----")) {
			  seat+=","+zw2;
		  }
		  if(!zw3.equals("-1=-----请选择座位-----")) {
			  seat+=","+zw3;
		  }
		  
		  user.setSeat(seat);
		  user.setCreateTime(MyUtils.getNowDateTime());
		  
		 
		  userService.addUser(user);
		  
		/*
		 * String zw = "100457457"; String yyrq = "2019-03-28"; String s = "08"; String
		 * st = "800"; String e = "22"; String et = "2200"; String sjc =
		 * System.currentTimeMillis()+"";
		 * 
		 * System.out.println(sjc);
		 * 
		 * Map<String, Object> qzParparamMap = new HashMap<String, Object>();
		 * 
		 * qzParparamMap.put("dev_id", zw); qzParparamMap.put("type","dev");
		 * qzParparamMap.put("start","2019-03-28+18:25");
		 * qzParparamMap.put("end","2019-03-28+19:25");
		 * qzParparamMap.put("start_time","1825"); qzParparamMap.put("end_time","1925");
		 * qzParparamMap.put("act","set_resv"); qzParparamMap.put("_",sjc);
		 * 
		 * // System.out.println(qzParams);
		 * 
		 * String url2 = "http://seat.hhit.edu.cn/ClientWeb/pro/ajax/reserve.aspx?" +
		 * "&dev_id={dev_id}" + "&type={type}" + "&start={start}" + "&end={end}" +
		 * "&start_time={start_time}" + "&end_time={end_time}" + "&act={act}" +
		 * "&_={_}"; String r = httpClient.qz2(url2, SESSION, qzParparamMap);
		 * System.out.println(r);
		 */
		
		return resultMap.success().message("注册成功");
	}
}
