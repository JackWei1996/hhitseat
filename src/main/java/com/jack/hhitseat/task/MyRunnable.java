
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
 * @author Aisino)chenwei
 */
//为了线程安全，不能依赖注入！！！
public class MyRunnable extends Thread {
	private static final Logger logger = LoggerFactory.getLogger(MyRunnable.class);
	private static LogServiceImpl logServiceImpl = (LogServiceImpl) AppBean.getBean("logServiceImpl");
	private static HttpClient httpClient = new HttpClient();
	private Map<String, String> sessionMap = new HashMap<>();
	private User u;
	
	private static  String url2 = "http://seat.hhit.edu.cn/ClientWeb/pro/ajax/reserve.aspx?" +
			"&dev_id={dev_id}" + "&type={type}" + "&start={start}" + "&end={end}" +
			"&start_time={start_time}" + "&end_time={end_time}" + "&act={act}" +
			"&_={_}"; 
	
	public MyRunnable() {
	}
	
	public MyRunnable(User u, Map<String, String> sessionMap ) {
		this.u = u;
		this.sessionMap = sessionMap;
	}
	
	@Override
	public void run() {
		String tem = u.getSeat();
		String[] seats = tem.split(",");
		for (String str : seats) {
			String seat = str.split("=")[0];
			String result = qz(sessionMap.get(u.getStuNum()),seat);
			String msg = result.split("\"msg\":\"")[1].split("\",\"data\":")[0];
			if(msg.contains("成功")) {
				Log log = new Log();
				log.setStatus(1);
				log.setUserId(Integer.parseInt(u.getStuNum()));
				log.setCreateTime(MyUtils.getNowDateTime());
				log.setCount(Integer.parseInt(seat));
				logServiceImpl.addLog(log);
				break;
			}else if(msg.contains("未登录")) {
				logger.warn("Session超时==={}",u.getUserName());
				MyTask task = new MyTask();
				String session = task.login(u.getStuNum(), u.getPassword());
				sessionMap.put(u.getStuNum(), session);
				seat = str.split("=")[0];
				result = qz(session,seat);
				msg = result.split("\"msg\":\"")[1].split("\",\"data\":")[0];
				if(msg.contains("成功")) {
					Log log = new Log();
					log.setStatus(1);
					log.setUserId(Integer.parseInt(u.getStuNum()));
					log.setCreateTime(MyUtils.getNowDateTime());
					log.setCount(Integer.parseInt(seat));
					logServiceImpl.addLog(log);
					break;
				}
			}
			logger.warn("{}==={}==={}",u.getUserName(),msg,str.split("=")[1]);
		}
	}
	
	
	public String qz(String session, String seat) {

		  String yyrq = MyUtils.getNextDate(); 

		  Date d = MyUtils.String2Date(yyrq);
		  
		  Calendar cal = Calendar.getInstance();
		  cal.setTime(d);
		  int w = cal.get(Calendar.DAY_OF_WEEK) - 1;

		  String r = "";

		if(w==3) {
			String sjc = System.currentTimeMillis()+"";
			
			Map<String, Object> qzParparamMap = new HashMap<String, Object>();
			
			qzParparamMap.put("dev_id", seat); 
			qzParparamMap.put("type","dev");
			qzParparamMap.put("start",yyrq+"+08:00");
			qzParparamMap.put("end",yyrq+"+14:00");
			qzParparamMap.put("start_time","800"); 
			qzParparamMap.put("end_time","1400");
			qzParparamMap.put("act","set_resv"); 
			qzParparamMap.put("_",sjc);
			
			r = httpClient.qz2(url2, session, qzParparamMap);
			
			sjc = System.currentTimeMillis()+"";
			
			Map<String, Object> qzParparamMap2 = new HashMap<String, Object>();
			
			qzParparamMap2.put("dev_id", seat); 
			qzParparamMap2.put("type","dev");
			qzParparamMap2.put("start",yyrq+"+17:30");
			//qzParparamMap2.put("end",yyrq+"+22:00");
			qzParparamMap2.put("end",yyrq+"+21:00");//暑假时间
			qzParparamMap2.put("start_time","1730"); 
			//qzParparamMap2.put("end_time","2200");
			qzParparamMap2.put("end_time","2100");		//暑假时间
			qzParparamMap2.put("act","set_resv"); 
			qzParparamMap2.put("_",sjc);

			r = httpClient.qz2(url2, session, qzParparamMap2);
		}else {
			String sjc = System.currentTimeMillis()+"";
			
			Map<String, Object> qzParparamMap = new HashMap<String, Object>();
			
			qzParparamMap.put("dev_id", seat); 
			qzParparamMap.put("type","dev");
			qzParparamMap.put("start",yyrq+"+08:00");
			//qzParparamMap.put("end",yyrq+"+22:00");
			qzParparamMap.put("end",yyrq+"+21:00");//暑假时间
			qzParparamMap.put("start_time","800"); 
			//qzParparamMap.put("end_time","2200");
			qzParparamMap.put("end_time","2100");		//暑假时间
			qzParparamMap.put("act","set_resv");
			qzParparamMap.put("_",sjc);
			
			r = httpClient.qz2(url2, session, qzParparamMap);
		}
		return r;
  }
}
