/*
 * All rights Reserved, Copyright (C) Aisino LIMITED 2018
 * FileName: ListenerTest.java
 * Version:  $Revision$
 * Modify record:
 * NO. |     Date       |    Name        |      Content
 * 1   | 2019年1月16日        | Aisino)Jack    | original version
 */
package com.jack.hhitseat.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * class name: CustomRealm <BR>
 * class description: 自定义 Realm <BR>
 * @version 1.00 2019年1月16日
 * @author Aisino)weihaohao
 */
@Component
public class CustomRealm extends AuthorizingRealm {
    /**logback日志记录*/
    private final Logger logger = LoggerFactory.getLogger(CustomRealm.class);

    /**
     * Method name: CustomRealm<BR>
     * Description: 通过构造器注入Mapper<BR>
     * @param userMapper 
     * @param userRoleMapper <BR>
     */
	/*
	 * @Autowired public CustomRealm(UserMapper userMapper,UserRoleMapper
	 * userRoleMapper) { this.userMapper = userMapper; this.userRoleMapper =
	 * userRoleMapper; }
	 */
    /**
     * @Override
     * @see org.apache.shiro.realm.AuthenticatingRealm#doGetAuthenticationInfo(org.apache.shiro.authc.AuthenticationToken) <BR>
     * Method name: doGetAuthenticationInfo <BR>
             * 获取身份验证信息
     * Description: Shiro中，最终是通过 Realm 来获取应用程序中的用户、角色及权限信息的。 <BR>
     * @param authenticationToken 用户身份信息 token
     * @return 返回封装了用户信息的 AuthenticationInfo 实例
     * @throws AuthenticationException  <BR>
    */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
    	logger.info("————身份认证————");
    	//获取token令牌
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String userName = "jack";
        if(!token.getUsername().equals(userName)) {
        	logger.warn("{}---用户不存在",token.getUsername());
        	//向前台抛出用户不存在json对象
        	throw new AccountException("USERNAME_NOT_EXIST");
        }
        String password = "jack2019..";
		
		/*
		 * if (null == password) { logger.warn("{}---用户不存在",token.getUsername());
		 * //向前台抛出用户不存在json对象 throw new AccountException("USERNAME_NOT_EXIST"); } else
		 * if (!password.equals(new String((char[]) token.getCredentials()))) {
		 * logger.warn("{}---输入密码错误",token.getUsername()); //向前台抛出输入密码错误json对象 throw new
		 * AccountException("PASSWORD_ERR"); }
		 */
		 
        logger.info("{}---身份认证成功",userName);
        Subject subject = SecurityUtils.getSubject();
        //设置shiro session过期时间(单位是毫秒!)
        subject.getSession().setTimeout(7_200_000);
        return new SimpleAuthenticationInfo(userName, password, getName());
    }

    /**
     * @Override
     * @see org.apache.shiro.realm.AuthorizingRealm#doGetAuthorizationInfo(org.apache.shiro.subject.PrincipalCollection) <BR>
     * Method name: doGetAuthorizationInfo <BR>
     * Description: 获取授权信息 <BR>
     * @param principalCollection
     * @return  <BR>
    */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
    	//从shro里面获取用户对象
    	String userName =  (String) SecurityUtils.getSubject().getPrincipal();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        //角色列表
        List<String> roles = null;
        //获得该用户角色
        roles.add("admin");
        Set<String> set = new HashSet<>();
        //需要将 role 封装到 Set 作为 info.setRoles() 的参数
        for (String role : roles) {			
        	set.add(role);
		}
        logger.warn("权限有---{}",roles);
        //设置该用户拥有的角色
        info.setRoles(set);
        return info;
    }
}