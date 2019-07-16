
 /*
 * All rights Reserved, Copyright (C) Aisino LIMITED 2019
 * FileName: AppBean.java
 * Version:  1.0
 * Modify record:
 * NO. |     Date       |    Name        |      Content
 * 1   | 2019年4月2日        | Aisino)Jack    | original version
 */
package com.jack.hhitseat.bean;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * class name: AppBean <BR>
 * class description: please write your description <BR>
 * @version 1.0  2019年4月2日 上午10:03:10
 * @author Aisino)Jack
 */
public class AppBean implements ApplicationContextAware {

	private static ApplicationContext applicationContext; 
	
	/**
	* @Override
	* @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext) <BR>
	* Method name: setApplicationContext <BR>
	* Description: please write your description <BR>
	* Remark: <BR>
	* @param applicationContext
	* @throws BeansException  <BR>
	*/
	@Override
	public void setApplicationContext(ApplicationContext appContext) throws BeansException {
		applicationContext = appContext;
	}
	
	public static Object getBean(String name){
        return applicationContext.getBean(name);
    }
    
     public static ApplicationContext getApplicationContext() {  
         return applicationContext;  
     }
}
