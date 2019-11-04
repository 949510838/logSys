package com.logSys.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.logSys.entity.All_log;
import com.logSys.entity.DeleteRate;
import com.logSys.entity.Log_source;
import com.logSys.entity.Logs;
import com.logSys.entity.Pagination;
import com.logSys.service.LogService;
import com.logSys.sessionManager.MySessionContext;
import com.logSys.util.DB2xls;
import com.logSys.util.ReadExcel;

//测试类
@Controller
public class test {
	private LogService logService;
	public void setLogService(LogService logService) {
		this.logService = logService;
	}
	
	
	//测试方法
	@RequestMapping(value="/test")
	@ResponseBody
	public String test(HttpServletRequest req) {
		//logService.deleteTask(); 
		
		return "测试成功";
	}
	
	@RequestMapping(value="/test1")
	public String test1(HttpServletRequest req) {
		System.out.println(1232);
		
//		return "forward:hello.html";
		return "redirect:hello.html";
	}
	
	
	
	
	/***读取项目中的文件***/
	public void printFilePath(HttpServletRequest req) {
		try {
			//1. 使用web.xml中的webAppRootKey的配置获取项目根路径
			System.out.println(System.getProperty("webApp.root"));
			//【推荐】2.getResourceAsStream("") 不写路径事表示默认到了该类包的首页目录下 : 普通项目在/src/下，发布后在/web-inf/classes/
			//InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("com/logSys/entity/All_log.xml");
			
			System.out.println(req.getSession().getServletContext().getContextPath()); //获取项目名，多用于重定向
			System.out.println(req.getSession().getServletContext().getRealPath("/"));  //获取项目的绝对路径
			
			File file = new File(req.getSession().getServletContext().getRealPath("/"));
		    //String [] fileName = file.list(); //获取该路径下的文件名。
		    System.out.println(new File(".").getAbsolutePath() + "============="); // "/"代表java虚拟机（jdk安装目录）所在盘符的根目录  .代表虚拟机当前运行的目录
			
		    InputStream resourceAsStream = new FileInputStream(new File(req.getSession().getServletContext().getRealPath("")+"/index.html"));
			BufferedReader br = new BufferedReader(new InputStreamReader(resourceAsStream));
			String s = "";
			while((s=br.readLine() )!= null) {
				System.out.println(s);
			}
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		
	}

	
}
