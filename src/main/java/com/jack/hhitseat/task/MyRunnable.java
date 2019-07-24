
 /*
 * All rights Reserved, Copyright (C) Aisino LIMITED 2019
 * FileName: MyRunnable.java
 * Version:  1.0
 * Modify record:
 * NO. |     Date       |    Name        |      Content
 * 1   | 2019年4月1日        | Aisino)Jack    | original version
 */
package com.jack.hhitseat.task;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jack.hhitseat.bean.AppBean;
import com.jack.hhitseat.bean.Log;
import com.jack.hhitseat.bean.User;
import com.jack.hhitseat.service.HttpClient;
import com.jack.hhitseat.service.impl.LogServiceImpl;
import com.jack.hhitseat.utils.MyUtils;

/**
 * class name: MyRunnable <BR>
 * class description: please write your description <BR>
 * @version 1.0  2019年4月1日 上午8:57:03
 * @author Aisino)Jack
 */
//为了线程安全，不能依赖注入！！！
public class MyRunnable extends Thread {
	private static final Logger logger = LoggerFactory.getLogger(MyRunnable.class);
	private static LogServiceImpl logServiceImpl = (LogServiceImpl) AppBean.getBean("logServiceImpl");
	private static HttpClient httpClient = new HttpClient();
	private String session;
	private String[] seats;
	private User u;
	private Map<String, Object> qzParparamMap = new HashMap<String, Object>();
	
	private static  String url2 = "http://seat.hhit.edu.cn/ClientWeb/pro/ajax/reserve.aspx?" +
			"&dev_id={dev_id}" + "&type={type}" + "&start={start}" + "&end={end}" +
			"&start_time={start_time}" + "&end_time={end_time}"+"&number={number}" + "&act={act}" +
			"&_={_}"; 
	
	public MyRunnable() {
	}
	
	public MyRunnable(User u
			, String session 
			, String[] seats
			, Map<String, Object> qzParparamMap) {
		this.u = u;
		this.session = session;
		this.seats = seats;
		this.qzParparamMap =qzParparamMap;
	}
	
	@Override
	public void run() {
		for (String str : seats) {
			String seat = str.split("=")[0];
			int i = 0;
			boolean success = false;
			//循环次数
			final int COUNT = 80;
			for (i = 0; i < COUNT; i++) {
				String result = qz(session, seat);
				String msg = result.split("\"msg\":\"")[1].split("\",\"data\":")[0];
				if(msg.contains("成功")) {
					logServiceImpl.addLog(u.getStuNum(), seat);
					success = true;
					break;
				}else if(msg.contains("冲突")) {
					logger.warn("{}==={}==={}==={}", i+1, u.getUserName(), msg, str.split("=")[1]);			
					break;
				}
			}
			if(success) {
				break;
			}
		}
	}
	
	public String qz(String session, String seat) {
		String sjc = System.currentTimeMillis()+"";
		httpClient.yzm(session);
		qzParparamMap.put("dev_id", seat); 
		qzParparamMap.put("_",sjc);
		
		return httpClient.qz2(url2, session, qzParparamMap);
  }
}
