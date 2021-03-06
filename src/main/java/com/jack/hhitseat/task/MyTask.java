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

import com.jack.hhitseat.bean.Login;
import com.jack.hhitseat.bean.User;
import com.jack.hhitseat.service.HttpClient;
import com.jack.hhitseat.service.impl.LogServiceImpl;
import com.jack.hhitseat.service.impl.LoginServiceImpl;
import com.jack.hhitseat.service.impl.UserServiceImpl;
import com.jack.hhitseat.utils.LoginVerify;
import com.jack.hhitseat.utils.MyUtils;

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
	@Autowired
	private LoginServiceImpl loginServiceImpl;
	
	private final Logger logger = LoggerFactory.getLogger(MyTask.class);
	
	private static Map<String, String> sessionMap = new HashMap<>();
	private static Map<String, String[]> seatMap = new HashMap<>();
	private static Map<String, Object> qzParparamMap = new HashMap<String, Object>();
	private static String VIEWSTATE = null;
	private static String EVENTVALIDATION = null;
	//线程池--设置线程数
	private static ExecutorService executorService = Executors.newFixedThreadPool(25);
	
	private static List<User> users = new ArrayList<>();
	
	private long successCount = 0;
	
	private final static String KEY = "16";
	
	//定时登录
	@Scheduled(cron = "0 25 5 * * ?") 
	@Scheduled(cron = "0 25 6 * * ?") 		//暑期抢座时间。
	//@Scheduled(cron = "0 25 8 * * ? ")
	@Scheduled(cron = "0 40 9 * * ? ")	//测试时间
    public void dl2() {
		successCount = logService.getSuccessNumb();
		if(successCount <= 0 ) {
			init();
			logger.warn("++++++结束登录");
		}
    }
	
	//请求对的验证码
	@Scheduled(cron = "0 27 5 * * ?") 
	@Scheduled(cron = "0 27 6 * * ?") 		//暑期抢座时间。
	//@Scheduled(cron = "0 25 8 * * ? ")
	@Scheduled(cron = "50 40 9 * * ? ")	//测试时间
    public void qqyzm() {
		logger.warn("++++++请求验证码");
		if(users.size()==0) {
    		init();
    	}
		if(successCount <= 0) {
			for (User user : users) {
				executorService.execute(
						new YzmRunnable(user, sessionMap, qzParparamMap)
						);
			}
		}
    }
	
	//定时抢座
    @Scheduled(cron = "0 30 5 * * ? ")
	@Scheduled(cron = "0 30 6 * * ? ")		//暑期抢座时间。
    //@Scheduled(cron = "0 30 8 * * ? ")
    @Scheduled(cron = "0 41 9 * * ? ")	//测试时间
	public void myTask() {
    	if(successCount <= 0) {
    		logger.warn("------启动抢座");
    		for (User u : users) {
    			//多线程抢座
    			executorService.execute(
    					new MyRunnable(u, sessionMap
    							, qzParparamMap)
    					);
    		}
    	}
	}
    
	//查看抢到座的人数
	@Scheduled(cron = "0 50 5 * * ? ")
	@Scheduled(cron = "0 50 6 * * ? ")		//暑期抢座时间。
	//@Scheduled(cron = "0 50 8 * * ? ")
	public void getResult() {
		if(successCount <= 0) {
			logger.warn("------结束抢座");
			long count = logService.getSuccessNumb();
			logger.warn("本次抢座成功人数==={}", count);
		}
	}
    
    public void init() {
    	sessionMap.clear();
    	seatMap.clear();
    	qzParparamMap.clear();
    	users.clear();
    	VIEWSTATE = null;
    	EVENTVALIDATION = null;
    	
    	users = userService.getAllUserByDo();
    	logger.warn("本次抢座人数==={}", users.size());
    	
    	Iterator<User> it = users.iterator();
    	while(it.hasNext()){
    		User user = it.next();
    		String sission = login(user.getStuNum(), user.getPassword());
    		//登录正常
			if(!sission.equals("LOGIN_ERR")) {
				String stuNum = user.getStuNum();
				//获取放入学号和Session
				sessionMap.put(stuNum, sission);
				//获取学号，和对应的座位
				seatMap.put(stuNum, user.getSeat().split(","));
			}else {
				it.remove();
				user.setIsdo(0);
				userService.updateUser(user);
				Login login = new Login();
				login.setUserId(Integer.parseInt(user.getStuNum()));
				login.setIp(user.getUserName()+"--密码错误！");
				login.setLoginTime(MyUtils.getNowDateTime());
				loginServiceImpl.addLogin(login);
			}
			logger.warn("{}==={}", user.getUserName(), sission);
    	}
    	
    	String yyrq = MyUtils.getNextDate(); 
		qzParparamMap.put("type","dev");
		qzParparamMap.put("start",yyrq+"+08:00");
		//qzParparamMap.put("end",yyrq+"+22:00");
		qzParparamMap.put("end",yyrq+"+21:00");		//暑假时间
		qzParparamMap.put("start_time","800"); 
		//qzParparamMap.put("end_time","2200");
		qzParparamMap.put("end_time","2100");		//暑假时间
		qzParparamMap.put("number", KEY);			//万能钥匙Key
		qzParparamMap.put("act","set_resv");
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
}
