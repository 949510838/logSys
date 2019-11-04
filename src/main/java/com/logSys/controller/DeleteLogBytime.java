package com.logSys.controller;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.logSys.service.LogService;

@Component
public class DeleteLogBytime {
	//ApplicationContext ac = new FileSystemXmlApplicationContext("beans.xml"); 
	private WebApplicationContext wac;
	//每天凌晨两点的定时任务（删除）
	@Scheduled(cron = "0 0 2 * * ?")
	public void taskDeleteLog() {		
		wac = ContextLoader.getCurrentWebApplicationContext();
		LogService logService = (LogService) wac.getBean("logService");
		logService.deleteTask();
		
	}
	
}
