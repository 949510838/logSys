package com.logSys.service;

import java.util.Date;
import java.util.List;

import com.logSys.dao.LogDao;
import com.logSys.entity.All_log;
import com.logSys.entity.DeleteRate;

public class LogService {
	private LogDao logDao;

	public void setLogDao(LogDao logDao) {
		this.logDao = logDao;
	}

	public List<All_log> getLogByCon(String sql) {
		List<All_log> list = logDao.findByCon(sql);
		return list;
	}
	public List<All_log> getLogByKey(String sql,String[] para, boolean kwIsNotNull) {
		List<All_log> list = logDao.findByKey(sql,para,kwIsNotNull);
		return list;
	}
	public List<All_log> getAllLog() {
		List<All_log> list = logDao.findAllLog();
		return list;
	}
	public void DeleteData(int[] arrid) {
		logDao.deleteById(arrid);
	}
	public void saveLog(All_log al) {
		logDao.save(al);
		
	}

	public DeleteRate getRate() {
		DeleteRate dr = logDao.getRate();
		return dr;
	}

	public void setRate(int rate) {
		logDao.setRate(rate);
	}
	public void deleteTask() {
		System.out.println("============开始自动任务删除日志================");
		//System.out.println("======================自动删除日志的logservice ----->>>" + new Date().toLocaleString() + "====================");
		logDao.deleteTask();
	}
	

	public void insertLogList(List<All_log> logList) {
		int i = 0;
		for(All_log al:logList) {
			System.out.println("============开始插入================" + i++);
			logDao.save(al);
		}
		
	}
	

	
	
}
