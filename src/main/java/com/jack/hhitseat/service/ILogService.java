
 /*
 * All rights Reserved, Copyright (C) Aisino LIMITED 2019
 * FileName: ILogService.java
 * Version:  1.0
 * Modify record:
 * NO. |     Date       |    Name        |      Content
 * 1   | 2019年3月29日        | Aisino)Jack    | original version
 */
package com.jack.hhitseat.service;

import com.jack.hhitseat.bean.Log;

/**
 * class name: ILogService <BR>
 * class description: please write your description <BR>
 * @version 1.0  2019年3月29日 下午12:58:47
 * @author Aisino)Jack
 */
public interface ILogService {
	public Long getAllCount();
	void addLog(String stuNum, String seat);
}
