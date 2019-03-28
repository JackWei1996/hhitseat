/**
 * 
 */
package com.jack.hhitseat.task;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.jack.hhitseat.bean.User;
import com.jack.hhitseat.service.HttpClient;
import com.jack.hhitseat.service.impl.UserServiceImpl;
import com.jack.hhitseat.utils.LoginVerify;
import com.jack.hhitseat.utils.MyUtils;

/**
 * @author 19604
 *
 */
@Component
@Controller
@EnableScheduling   // 开启定时任务
@EnableAsync        // 开启多线程
public class MyTask {
	@Autowired
	HttpClient httpClient;
	@Autowired
	private UserServiceImpl userService;
	private final Logger logger = LoggerFactory.getLogger(MyTask.class);
	
	private static Map<String, String> sessionMap = new HashMap<>();
	private static String VIEWSTATE = null;
	private static String EVENTVALIDATION = null;
	
	private static List<User> users = new ArrayList<>();
	
	//添加定时任务
    @Scheduled(cron = "0 0,29,30,31 5 * * ? ")
	public void myTask() {
		for (User u : users) {
			String tem = u.getSeat();
			String[] seats = tem.split(",");
			for (String str : seats) {
				String seat = str.split("=")[0];
				String result = qz(sessionMap.get(u.getStuNum()),seat);
				logger.info("{}==={}",u.getUserName(),result.split("\"msg\":\"")[1].split("\",\"data\":")[0]);
			}
		}
		logger.info("抢座完毕");
	}
    
   	@Scheduled(cron = "0 55 4 * * ? ")
    public void dl1() {
    	logger.info("登录任务1");
    	init();
    }
    
    @Scheduled(cron = "0 25 5 * * ?") 
    public void dl2() {
    	logger.info("登录任务2");
    	init();
    }
    
    public void init() {
    	users = userService.getAllUserByDo();
		for (User user : users) {
			sessionMap.put(user.getStuNum(), login(user.getStuNum(), user.getPassword()));
		}
    }
    
    public String login(String stuNum, String pass) {
    	if(VIEWSTATE==null || EVENTVALIDATION==null) {
			  VIEWSTATE = LoginVerify.getVIEWSTATE();
			  EVENTVALIDATION = LoginVerify.getEVENTVALIDATION();
		}
    	 String url = "http://seat.hhit.edu.cn/pages/ic/LoginForm.aspx"; 
		  HttpMethod method = HttpMethod.POST; 
		  MultiValueMap<String, Object> params = new LinkedMultiValueMap<String, Object>();

		  params.add("__VIEWSTATE",VIEWSTATE);
		  params.add("__EVENTVALIDATION", EVENTVALIDATION); 
		  params.add("szLogonName", stuNum); 
		  params.add("szMiss", pass);
		  params.add("Button_Logon", "登录");
		  
		return httpClient.client(url, method, params);
    }
    
    public String qz(String session, String seat) {

		  String yyrq = MyUtils.getNextDate(); 
 
		  Date d = MyUtils.String2Date(yyrq);
		  
		  Calendar cal = Calendar.getInstance();
		  cal.setTime(d);
		  int w = cal.get(Calendar.DAY_OF_WEEK) - 1;

		String r = "";
		String url2 = "http://seat.hhit.edu.cn/ClientWeb/pro/ajax/reserve.aspx?" +
				"&dev_id={dev_id}" + "&type={type}" + "&start={start}" + "&end={end}" +
				"&start_time={start_time}" + "&end_time={end_time}" + "&act={act}" +
				"&_={_}"; 

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
			qzParparamMap2.put("end",yyrq+"+22:00");
			qzParparamMap2.put("start_time","1730"); 
			qzParparamMap2.put("end_time","2200");
			qzParparamMap2.put("act","set_resv"); 
			qzParparamMap2.put("_",sjc);

			r = httpClient.qz2(url2, session, qzParparamMap2);
		}else {
			String sjc = System.currentTimeMillis()+"";
			
			Map<String, Object> qzParparamMap = new HashMap<String, Object>();
			
			qzParparamMap.put("dev_id", seat); 
			qzParparamMap.put("type","dev");
			qzParparamMap.put("start",yyrq+"+08:00");
			qzParparamMap.put("end",yyrq+"+22:00");
			qzParparamMap.put("start_time","800"); 
			qzParparamMap.put("end_time","2200");
			qzParparamMap.put("act","set_resv");
			qzParparamMap.put("_",sjc);
			
			r = httpClient.qz2(url2, session, qzParparamMap);
		}
		return r;
    }
    
}
