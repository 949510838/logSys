package com.logSys.test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.logSys.dao.LogDao;
import com.logSys.service.LogService;

@Controller
public class test01 {
	
	@Autowired
	@Qualifier("logService")
	private LogService logService;
	
	
	//springmvc默认返回字符串编码格式是 text/plain;charset=ISO-8859-1（在浏览器响应头Content-Type），返回中文会乱码，使用produces属性可以设置返回数据的编码为utf-8
	//也可以配置统一的StringHttpMessageConverter消息转换（在配置文件中配置）
	@RequestMapping(value="/test01",produces="text/json;charset=UTF-8")
	@ResponseBody
	public String test01(HttpServletRequest requset,HttpServletResponse response) {
		System.out.println(logService);
		System.out.println(requset);
		return "张三";
	}
	
	@RequestMapping(value="/test02")
	public String test02(HttpServletRequest req) {
		System.out.println(1232);
		
		//return "forward:hello.html";
		return "redirect:hello.html";
	}
	
	
	

}
