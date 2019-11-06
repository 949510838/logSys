package com.logSys.aop;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.logSys.controller.LogController;

/** 
* @author BieHongLi 
* @version 创建时间：2017年3月28日 下午9:10:43 
* @Aspect:指定当前类为切面类
*/
@Component  //加入到IoC容器
@Aspect  //指定当前类为切面类
public class PrintLog4j {
    
	private static Logger log = Logger.getLogger(PrintLog4j.class);
	
	public PrintLog4j() {
		super();
		//System.out.println("调用了PrintLog4j aop 的构造方法");
	}
    
	//指定切入点表达式，拦截那些方法，即为那些类生成代理对象
    //@Pointcut("execution(* com.bie.aop.UserDao.save(..))")  ..代表所有参数
    //@Pointcut("execution(* com.bie.aop.UserDao.*())")  指定所有的方法
    //@Pointcut("execution(* com.bie.aop.UserDao.save())") 指定save方法
    @Pointcut("execution(* com.logSys.controller.*.*(..))")
    public void pointCut(){
    	//System.out.println("LogAspect----logStart");
    }
    
    @Before("pointCut()")
    public void begin(){
        //System.out.println("aop切点方法调用前执行-before");
    }
    
    @After("pointCut()")
    public void close(){
        //System.out.println("aop切点方法调用后执行-after");
    }
    
    @AfterThrowing(pointcut = "pointCut()", throwing = "e")
    public void handleException(Exception e) {	//如果在@Around中捕获异常，这里将不再调用
    	System.out.println("出现异常-afterthrowing");
    	//log.error("error",e);
    }
    
    @Around("pointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
    	Object obj = null;
    	try {
    		//System.out.println("aop切点方法调用前执行-around");
    		//继续执行切点方法
    		obj = joinPoint.proceed();
			//System.out.println("aop切点方法调用后执行-around");
		} catch (Throwable e) {
			
			System.out.println("出现异常-around");
			e.printStackTrace();
	    	log.error("error",e);
	    	//throw e;
	    	
		}
    	return obj;
    }
    
}