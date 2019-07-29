package com.jack.hhitseat.task;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jack.hhitseat.bean.AppBean;
import com.jack.hhitseat.bean.User;
import com.jack.hhitseat.service.HttpClient;
import com.jack.hhitseat.service.impl.LogServiceImpl;

public class YzmRunnable extends Thread{
	private final Logger logger = LoggerFactory.getLogger(MyRunnable.class);
	private static HttpClient httpClient = new HttpClient();
	private User user;
	private Map<String, String> sessionMap = new HashMap<>();
	private Map<String, Object> qzParparamMap = new HashMap<String, Object>();
	private static LogServiceImpl logServiceImpl = (LogServiceImpl) AppBean.getBean("logServiceImpl");
	
	private final static  String BOOK_URL = "http://seat.hhit.edu.cn/ClientWeb/pro/ajax/reserve.aspx?" +
			"&dev_id={dev_id}" + "&type={type}" + "&start={start}" + "&end={end}" +
			"&start_time={start_time}" + "&end_time={end_time}"+"&number={number}" + "&act={act}" +
			"&_={_}"; 
	
	public YzmRunnable() {
	}
	public YzmRunnable(User user
			, Map<String, String> sessionMap
			, Map<String, Object> qzParparamMap) {
		this.user = user;
		this.sessionMap = sessionMap;
		this.qzParparamMap =qzParparamMap;
	}
	
	@Override
	public synchronized void run() {
		final int COUNT = 200;
		String stuNum = user.getStuNum();
		String result = "";
		String session = sessionMap.get(stuNum);
		int i = 0;
		String seatNumb = user.getSeat().split(",")[0].split("=")[0];
		String seatName = user.getSeat().split(",")[0].split("=")[1];
		
		for (i = 1; i <= COUNT; i++) {
			String sjc = System.currentTimeMillis()+"";
			httpClient.yzm(session);
			qzParparamMap.put("dev_id", seatNumb); 
			qzParparamMap.put("_",sjc);
			result = httpClient.qz2(BOOK_URL, session, qzParparamMap);
			if(!result.contains("验证码")) {
				logger.warn("{}==={}==={}==={}", i, user.getUserName()
						, result.split("\"msg\":\"")[1].split("\",\"data\":")[0]
						, seatName);
				if(result.contains("成功")) {
					logServiceImpl.addLog(stuNum, seatNumb);
				}
				break;
			}
		}
	}
}
