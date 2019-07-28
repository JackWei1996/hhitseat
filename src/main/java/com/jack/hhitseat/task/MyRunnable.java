 /** All rights Reserved, Copyright (C) Aisino LIMITED 2019
 * FileName: MyRunnable.java
 * Version:  1.0
 * Modify record:
 * NO. |     Date       |    Name        |      Content
 * 1   | 2019年4月1日        | Aisino)Jack    | original version
 */
package com.jack.hhitseat.task;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jack.hhitseat.bean.AppBean;
import com.jack.hhitseat.bean.User;
import com.jack.hhitseat.service.HttpClient;
import com.jack.hhitseat.service.impl.LogServiceImpl;

/**
 * class name: MyRunnable <BR>
 * class description: please write your description <BR>
 * @version 1.0  2019年4月1日 上午8:57:03
 * @author Aisino)Jack
 */
//为了线程安全，不能依赖注入！！！
public class MyRunnable extends Thread {
	private final Logger logger = LoggerFactory.getLogger(MyRunnable.class);
	private static HttpClient httpClient = new HttpClient();
	private static LogServiceImpl logServiceImpl = (LogServiceImpl) AppBean.getBean("logServiceImpl");
	private User u;
	private Map<String, String> sessionMap = new HashMap<>();
	private Map<String, Object> qzParparamMap = new HashMap<String, Object>();
	
	private final static  String BOOK_URL = "http://seat.hhit.edu.cn/ClientWeb/pro/ajax/reserve.aspx?" +
			"&dev_id={dev_id}" + "&type={type}" + "&start={start}" + "&end={end}" +
			"&start_time={start_time}" + "&end_time={end_time}"+"&number={number}" + "&act={act}" +
			"&_={_}"; 
	
	public MyRunnable() {
	}
	
	public MyRunnable(User u
			, Map<String, String> sessionMap
			, Map<String, Object> qzParparamMap) {
		this.u = u;
		this.sessionMap = sessionMap;
		this.qzParparamMap =qzParparamMap;
	}
	
	@Override
	public synchronized void run() {
		String stuNum = u.getStuNum();
		String[] seats = u.getSeat().split(",");
		for (String str : seats) {
			String seat = str.split("=")[0];
			String result = "";
			String sjc = System.currentTimeMillis()+"";
			qzParparamMap.put("dev_id", seat); 
			qzParparamMap.put("_",sjc);
			
			result = httpClient.qz2(BOOK_URL, sessionMap.get(stuNum), qzParparamMap);
			
			if(result.contains("成功")) {
				logServiceImpl.addLog(u.getStuNum(), seat);
				break;
			}
			//测试输出
			logger.warn("{}==={}==={}", u.getUserName()
					, result.split("\"msg\":\"")[1].split("\",\"data\":")[0]
					, str.split("=")[1]);
		}
		
	}
}
