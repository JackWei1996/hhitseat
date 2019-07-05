/**
 * 
 */
package com.jack.hhitseat.service;

import java.util.List;

import com.jack.hhitseat.bean.User;
import com.jack.hhitseat.model.ResultMap;

/**
 * @author 19604
 *
 */
public interface UserService {
	public List<User> getUserByNum(String num);

	public void addUser(User user);
	
	public List<User> getAllUserByDo();
	
	public int updateIsDo(String num, Integer flag);
}
