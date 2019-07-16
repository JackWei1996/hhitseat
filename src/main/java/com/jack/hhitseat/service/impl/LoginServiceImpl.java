
 /*
 * All rights Reserved, Copyright (C) Aisino LIMITED 2019
 * FileName: LoginServiceImpl.java
 * Version:  1.0
 * Modify record:
 * NO. |     Date       |    Name        |      Content
 * 1   | 2019年3月29日        | Aisino)Jack    | original version
 */
package com.jack.hhitseat.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jack.hhitseat.bean.Login;
import com.jack.hhitseat.mapper.LoginMapper;
import com.jack.hhitseat.service.ILoginService;

/**
 * class name: LoginServiceImpl <BR>
 * class description: please write your description <BR>
 * @version 1.0  2019年3月29日 下午1:06:03
 * @author Aisino)Jack
 */
@Service
public class LoginServiceImpl implements ILoginService {

	
	private final Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);
	
	@Autowired
	LoginMapper loginMapper;
	/**
	* @Override
	* @see com.jack.hhitseat.service.ILoginService#addLogin(com.jack.hhitseat.bean.Login) <BR>
	* Method name: addLogin <BR>
	* Description: please write your description <BR>
	* Remark: <BR>
	* @param login  <BR>
	*/
	@Override
	public void addLogin(Login login) {
		try {
			loginMapper.insert(login);
		} catch (Exception e) {
			logger.error("添加登录记录出错", e);
		}
	}

}
