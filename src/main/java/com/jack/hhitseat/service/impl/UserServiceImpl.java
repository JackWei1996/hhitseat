/**
 * 
 */
package com.jack.hhitseat.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jack.hhitseat.controller.LoginController;
import com.jack.hhitseat.model.ResultMap;
import com.jack.hhitseat.service.UserService;

/**
 * @author 19604
 *
 */
public class UserServiceImpl implements UserService {
	private ResultMap resultMap;
	private final Logger logger = LoggerFactory.getLogger(LoginController.class);

	@Override
	public ResultMap login(String username, String password) {
		// 从SecurityUtils里边创建一个 subject
		Subject subject = SecurityUtils.getSubject();
		// 在认证提交前准备 token（令牌）
		UsernamePasswordToken token = new UsernamePasswordToken(username, password);
		// 执行认证登陆
		subject.login(token);
		// 根据权限，指定返回数据
		List<String> role = new ArrayList<>();
		if (!role.isEmpty()) {
			logger.info("欢迎登录------您的权限是{}",role);
			return resultMap.success().message("欢迎登陆");
		}
		return resultMap.fail().message("权限错误！");
	}
	
}
