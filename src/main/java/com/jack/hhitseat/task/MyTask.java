/**
 * 
 */
package com.jack.hhitseat.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
import com.jack.hhitseat.service.impl.LogServiceImpl;
import com.jack.hhitseat.service.impl.UserServiceImpl;
import com.jack.hhitseat.utils.LoginVerify;

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
	@Autowired
	private LogServiceImpl logService;
	
	private final Logger logger = LoggerFactory.getLogger(MyTask.class);
	
	private static Map<String, String> sessionMap = new HashMap<>();
	private static String VIEWSTATE = null;
	private static String EVENTVALIDATION = null;
	//线程池--设置线程数
	private static ExecutorService executorService = Executors.newFixedThreadPool(25);
	
	private static List<User> users = new ArrayList<>();
	
	private long successCount = 0;
	
	//定时登录
	@Scheduled(cron = "0 25 5 * * ?") 
	@Scheduled(cron = "0 25 6 * * ?") 		//暑期抢座时间。
    public void dl2() {
    	logger.warn("++++++启动登录");
    	init();
    	logger.warn("++++++结束登录");
    	successCount = logService.getSuccessNumb();
    }
	
	//添加定时任务
    @Scheduled(cron = "0 30 5 * * ? ")
	@Scheduled(cron = "0 30 6 * * ? ")		//暑期抢座时间。
	public void myTask() {
    	logger.warn("------启动抢座");
    	if(users.size()==0) {
    		init();
    	}
    	if(successCount <= 0) {
    		for (User u : users) {
    			//多线程抢座
    			executorService.execute(new MyRunnable(u, sessionMap));
    		}
    	}
	}
    
	//查看抢到座的人数
	@Scheduled(cron = "0 50 5 * * ? ")
	@Scheduled(cron = "0 50 6 * * ? ")		//暑期抢座时间。
	public void getResult() {
		logger.warn("------结束抢座");
		long count = logService.getSuccessNumb();
		logger.warn("本次抢座成功人数==={}", count);
	}
    
    public void init() {
    	sessionMap.clear();
    	VIEWSTATE = null;
    	EVENTVALIDATION = null;
    	users.clear();
    	
    	users = userService.getAllUserByDo();
    	logger.warn("本次抢座人数==={}", users.size());
    	
    	Iterator<User> it = users.iterator();
    	while(it.hasNext()){
    		User user = it.next();
    		String s = login(user.getStuNum(), user.getPassword());
			if(!s.equals("LOGIN_ERR")) {//正常
				sessionMap.put(user.getStuNum(), s);
			}else {
				it.remove();
				user.setIsdo(0);
				userService.updateUser(user);
			}
			logger.warn("{}==={}", user.getUserName(), s);
    	}
    }
    
    public String login(String stuNum, String pass) {
    	if(VIEWSTATE==null || EVENTVALIDATION==null) {
			VIEWSTATE = LoginVerify.getVIEWSTATE();
			EVENTVALIDATION = LoginVerify.getEVENTVALIDATION();
		    logger.warn("VIEWSTATE ==== {}", VIEWSTATE);
		    logger.warn("EVENTVALIDATION ==== {}", EVENTVALIDATION);
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
}
