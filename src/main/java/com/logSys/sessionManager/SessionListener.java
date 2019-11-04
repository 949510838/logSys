package com.logSys.sessionManager;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class SessionListener implements HttpSessionListener {  

    private MySessionContext myc = MySessionContext.getInstance();  

    //当系统创建session对象后，会通过web.xml中间的session监听器，自动调用这里的Create方法向自定义的sessionContext中添加
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {  
        HttpSession session = httpSessionEvent.getSession();  
        System.out.println("===========session被创建了,ID："+session.getId()+"===========");
        myc.addSession(session);  
    }  
    //当系统销毁session后自动调用destory方法，从sessioncontext中删除该session对象
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {  
        HttpSession session = httpSessionEvent.getSession();  
        myc.delSession(session);  
    }  

}  