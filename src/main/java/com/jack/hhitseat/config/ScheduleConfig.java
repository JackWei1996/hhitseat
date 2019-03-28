
 /*
 * All rights Reserved, Copyright (C) Aisino LIMITED 2019
 * FileName: ScheduleConfig.java
 * Version:  1.0
 * Modify record:
 * NO. |     Date       |    Name        |      Content
 * 1   | 2019年3月28日        | Aisino)Jack    | original version
 */
package com.jack.hhitseat.config;

import java.util.concurrent.Executors;

import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

/**
 * class name: ScheduleConfig <BR>
 * class description: please write your description <BR>
 * @version 1.0  2019年3月28日 下午1:09:52
 * @author Aisino)weihaohao
 */
public class ScheduleConfig implements SchedulingConfigurer{
	@Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        //设定一个长度10的定时任务线程池
        taskRegistrar.setScheduler(Executors.newScheduledThreadPool(5));
    }
}
