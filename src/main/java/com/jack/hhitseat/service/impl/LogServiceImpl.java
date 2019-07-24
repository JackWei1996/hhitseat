
 /*
 * All rights Reserved, Copyright (C) Aisino LIMITED 2019
 * FileName: LogServiceImpl.java
 * Version:  1.0
 * Modify record:
 * NO. |     Date       |    Name        |      Content
 * 1   | 2019年3月29日        | Aisino)Jack    | original version
 */
package com.jack.hhitseat.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jack.hhitseat.bean.Log;
import com.jack.hhitseat.bean.LogExample;
import com.jack.hhitseat.mapper.LogMapper;
import com.jack.hhitseat.service.ILogService;
import com.jack.hhitseat.utils.MyUtils;

/**
 * class name: LogServiceImpl <BR>
 * class description: please write your description <BR>
 * @version 1.0  2019年3月29日 下午1:01:05
 * @author Aisino)Jack
 */
@Service("logServiceImpl")
public class LogServiceImpl implements ILogService {
	
	@Autowired
	LogMapper logMapper;
	
	private final Logger logger = LoggerFactory.getLogger(LogServiceImpl.class);

	/**
	* @Override
	* @see com.jack.hhitseat.service.ILogService#addLog(com.jack.hhitseat.bean.Log) <BR>
	* Method name: addLog <BR>
	* Description: please write your description <BR>
	* Remark: <BR>
	* @param log  <BR>
	*/
	@Override
	public void addLog(String stuNum, String seat) {
		Log log = new Log();
		log.setStatus(1);
		log.setCreateTime(MyUtils.getNowDateTime());
		log.setUserId(Integer.parseInt(stuNum));
		log.setCount(Integer.parseInt(seat));
		try {
			logMapper.insert(log);
		} catch (Exception e) {
			logger.error("添加日志出错", e);
		}
	}

	public long getSuccessNumb() {
		List<Log> logs = new ArrayList<>();
		LogExample example = new LogExample();
		example.createCriteria().andCreateTimeBetween(MyUtils.getNowDateYMD(), MyUtils.getNextDate());
		try {
			logs = logMapper.selectByExample(example);
		} catch (Exception e) {
			logger.error("查询日志出错", e);
		}
		return logs.size();
	}

	@Override
	public Long getAllCount() {
		LogExample example = new LogExample();
		return logMapper.countByExample(example);
	}

}
