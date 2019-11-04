package com.logSys.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.logSys.dao.LogDao;

public class test01 {
	LogDao logDao;


	public void setLogDao(LogDao logDao) {
		this.logDao = logDao;
	}


	public LogDao getLogDao() {
		return logDao;
	}


	public static void main(String[] args) {
		System.out.println(123);

		ApplicationContext ac = new FileSystemXmlApplicationContext("springmvc.xml");
		test01 ts = (test01)ac.getBean("test01");
		ts.getLogDao().deleteTask();

	}

}
