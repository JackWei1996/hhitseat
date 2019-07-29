package com.jack.hhitseat.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jack.hhitseat.bean.User;
import com.jack.hhitseat.utils.MyUtils;

public class YzmRunnableTest {
	
	public static void main(String[] args) {
		Map<String, String> sessionMap = new HashMap<>();
		Map<String, Object> qzParparamMap = new HashMap<String, Object>();
		List<User> users = new ArrayList<>();
		final String KEY = "16";
		User userItem1 = new User();
		userItem1.setUserName("姓名");
		userItem1.setStuNum("123456");
		userItem1.setSeat("100457467=西501-014,100457468=西501-015,100457469=西501-016");
		users.add(userItem1);
		
		User userItem2 = new User();
		userItem2.setUserName("姓名");
		userItem2.setStuNum("654321");
		userItem2.setSeat("100457635=西501-184,100457636=西501-185,100457637=西501-186");
		users.add(userItem2);
		
		sessionMap.put("123456", "vm3u3d1oq4gqnj52kmnkdich");
		sessionMap.put("654321", "vm3u3d1oq4gqnj52kmnkdich");
		
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
		
		//for(User user : users) {
			YzmRunnable task = new YzmRunnable(users.get(0), sessionMap, qzParparamMap);
			task.run();
		//}
		
	}
}
