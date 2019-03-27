/**
 * 
 */
package com.jack.hhitseat.service.impl;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jack.hhitseat.bean.User;
import com.jack.hhitseat.bean.UserExample;
import com.jack.hhitseat.mapper.UserMapper;
import com.jack.hhitseat.service.UserService;

/**
 * @author 19604
 *
 */
@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	UserMapper userMapper;

	@Override
	public List<User> getUserByNum(String num) {
		UserExample example = new UserExample();
		example.createCriteria().andStuNumEqualTo(num);
		List<User> user = new ArrayList<>();
		try {			
			user = userMapper.selectByExample(example);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return user;
	}

	@Override
	public void addUser(User user) {
		try {
			userMapper.insert(user);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<User> getAllUserByDo() {
		UserExample example = new UserExample();
		example.createCriteria().andIsdoEqualTo(1);
		List<User> user = new ArrayList<>();
		try {			
			user = userMapper.selectByExample(example);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return user;
	}
	
}
