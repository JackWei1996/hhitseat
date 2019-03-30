/**
 * 
 */
package com.jack.hhitseat.service.impl;


import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	
	private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Override
	public List<User> getUserByNum(String num) {
		UserExample example = new UserExample();
		example.createCriteria().andStuNumEqualTo(num);
		List<User> user = new ArrayList<>();
		try {			
			user = userMapper.selectByExample(example);
		} catch (Exception e) {
			logger.error("根据学号获取学生出错",e);
		}
		
		return user;
	}

	@Override
	public void addUser(User user) {
		try {
			userMapper.insert(user);
		} catch (Exception e) {
			logger.error("注册异常",e);
		}
	}

	public List<User> getAllUserByDo() {
		UserExample example = new UserExample();
		example.createCriteria().andIsdoEqualTo(1);
		example.setOrderByClause("seat ASC");
		List<User> user = new ArrayList<>();
		try {			
			user = userMapper.selectByExample(example);
		} catch (Exception e) {
			logger.error("查询所有待抢座学生异常",e);
		}
		
		return user;
	}

	public void updateUser(User user) {
		UserExample example = new UserExample();
		example.createCriteria().andIdEqualTo(user.getId());
		
		try {			
			userMapper.updateByExampleSelective(user, example);
		} catch (Exception e) {
			logger.error("更新用户异常",e);
		}
	}
	
}
