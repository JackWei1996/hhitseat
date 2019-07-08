
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jack.hhitseat.bean.Login;
import com.jack.hhitseat.bean.User;
import com.jack.hhitseat.model.ResultMap;
import com.jack.hhitseat.service.HttpClient;
import com.jack.hhitseat.service.impl.LoginServiceImpl;
import com.jack.hhitseat.service.impl.UserServiceImpl;
import com.jack.hhitseat.utils.LoginVerify;
import com.jack.hhitseat.utils.MyUtils;

/**
 * class name: IndexController <BR>
 * class description: please write your description <BR>
 * @version 1.0  2019年3月27日 上午10:28:50
 * @author Aisino)JackWei
 */
@Controller
public class IndexController {
	@Autowired
	HttpClient httpClient;
	@Autowired
	private UserServiceImpl userService;
	@Autowired
	private LoginServiceImpl loginService;
	
	private final Logger logger = LoggerFactory.getLogger(IndexController.class);
	
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
			return resultMap.fail().message("学号密码错误！");
		}
			
		List<User> users = userService.getUserByNum(u);

		if(users.size()>0) {
			return resultMap.fail().message("该学号已经被注册！");
		}
		  
		  String loginUrl = "http://seat.hhit.edu.cn/ClientWeb/xcus/ic2/Default.aspx";
		  
		  String html = httpClient.login(loginUrl, SESSION);
		  
		  String name = html.split("acc.name = \"")[1].split("\";")[0];
		  
		  if(name==null || name.equals("")) {
				return resultMap.fail().message("学号密码错误！");
		  }
		  
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
		  
		return resultMap.success().message("注册成功");
	}
	
	@RequestMapping(value="/switch")
	public String onOff() {
		return "oo";
	}
	
	@RequestMapping(value="/toOn")
	@ResponseBody
	public Object toOn(String u, String p,String em,
			String f) {

		Login login = new Login();

		List<User> users = userService.getUserByNum(u);
		
		if(users.size() <= 0) {
			return resultMap.fail().message("此学号没有注册！");
		}
		User user = users.get(0);

		if(!user.getPassword().equals(p)) {
			return resultMap.fail().message("密码输入错误！");
		}else if(!user.getEmail().equals(em)) {
			return resultMap.fail().message("邮箱输入错误！");
		}
		if(f.equals(user.getIsdo()+"")&&f.equals("0")){
			return resultMap.success().message("已经是关闭状态!");
		}else if(f.equals(user.getIsdo()+"")&&f.equals("1")){
			return resultMap.success().message("已经是开启状态!");
		}
		
		int result = -1;
		try {
			login.setUserId(Integer.parseInt(u));
			result = userService.updateIsDo(u, Integer.parseInt(f));
		}catch (Exception e) {
			return resultMap.fail().message("输入参数错误！");
		}
		
		if(f.equals("0") && result ==1) {
			//logger.warn("{}===关闭预约服务成功", user.getUserName());
			login.setIp(user.getUserName()+"--关闭服务");
			login.setLoginTime(MyUtils.getNowDateTime());
			loginService.addLogin(login);
			return resultMap.success().message("关闭预约服务成功");
		}else if(f.equals("1") && result ==1){
			//logger.warn("{}===开启预约服务成功", user.getUserName());
			login.setIp(user.getUserName()+"--开启服务");
			login.setLoginTime(MyUtils.getNowDateTime());
			loginService.addLogin(login);
			return resultMap.success().message("开启预约服务成功");
		}
		return resultMap.success().message("设置成功");
	}
	
}
