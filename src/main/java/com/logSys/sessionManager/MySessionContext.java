package com.logSys.sessionManager;

import java.util.HashMap;

import javax.servlet.http.HttpSession;

public class MySessionContext {  
    private static MySessionContext instance;  
    private HashMap<String,HttpSession> sessionMap;  

    private MySessionContext() {  
        sessionMap = new HashMap<String,HttpSession>();  
    }  
    //单例类获取对象，不能直接new.双检锁/双重校验锁（DCL，即 double-checked locking）
    public static MySessionContext getInstance() {  
        if(instance == null) {
        	synchronized (MySessionContext.class) {
            	if (instance == null) {  
                    instance = new MySessionContext();  
                }  
    		}   	
        } 	
        return instance;  
    }  

    public synchronized void addSession(HttpSession session) {  
        if (session != null) {  
            sessionMap.put(session.getId(), session);  
        }  
    }  

    public synchronized void delSession(HttpSession session) {  
        if (session != null) {  
            sessionMap.remove(session.getId());  
        }  
    }  

    public synchronized HttpSession getSession(String sessionID) {  
        System.out.println(sessionID);
        System.out.println(sessionMap);
    	if (sessionID == null) {  
            return null;  
        }  
        return sessionMap.get(sessionID);  
    }  

}  