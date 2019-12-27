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
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.logSys.common.result.ErrorMsg;
import com.logSys.common.result.SuccessMsg;
import com.logSys.entity.All_log;
import com.logSys.entity.DeleteRate;
import com.logSys.entity.Log_source;
import com.logSys.entity.Logs;
import com.logSys.entity.Pagination;
import com.logSys.service.LogService;
import com.logSys.util.DB2xls;
import com.logSys.util.ReadExcel;

@Controller
public class LogController {
	private static Logger log = Logger.getLogger(LogController.class);
	private static Executor pool = Executors.newCachedThreadPool(); //线程池对象
	private LogService logService;
	public void setLogService(LogService logService) {
		this.logService = logService;
	}
	
	private DB2xls dB2xls ;
	public void setdB2xls(DB2xls dB2xls) {
		this.dB2xls = dB2xls;
	}
	
	//查询入口(旧)
	@RequestMapping(value="/getLogByKey")
	@ResponseBody	//@ResponseBody放在方法上面和返回值前面作用一样
	public List<Logs> getLogByKey(@RequestParam(value="para[]", required = false) String[] para,boolean kwIsNotNull, String sql,int currentPage,int pageSize,HttpServletRequest req) throws Exception{
		System.out.println(sql);
		System.out.println(para);
		
		//this.printFilePath(req);
		List<All_log>  li = logService.getLogByKey(sql,para,kwIsNotNull);
		req.getSession().setAttribute("li", li);

		Pagination pt = new Pagination();
		pt.setCurrentPage(currentPage);
		pt.setTotalCount(li.size());
		pt.setPaginalCount(pageSize);
		
		for (int i=pt.getCurrentPageBegan();i<=pt.getCurrentPageEnd();i++) {
			if(i<li.size()) {
				pt.getList().add(li.get(i));
			}else {
				break;
			}
		}		
		List<Logs> list = new ArrayList<Logs>();
		for(All_log eachlog:pt.getList()){
			Logs ls = new Logs();
			ls.setContent(eachlog.getContent());
			ls.setId(eachlog.getId());
			ls.setSource_name(eachlog.getLs().getSource_name());
			ls.setLog_type(eachlog.getLog_type());
			ls.setOperator(eachlog.getOperator());
			
			SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //设置格式
			String timeText=format.format(new Date(eachlog.getLog_date()));  
			
			ls.setLog_date(timeText);
			ls.setRemarks(eachlog.getRemarks());
			ls.setTotalC(li.size());
			list.add(ls);			
		}
		System.out.println("共查询 "+li.size()+"条记录");
		return list;
	}
	
	//日志查询入口（分页查询）
	@RequestMapping(value="/getLogByCondition")
	@ResponseBody
	public String getLogByCondition(@RequestParam(value="para[]", required = false) String[] para,boolean kwIsNotNull, String sql,int pageSize,HttpServletRequest req) throws Exception {
		HashMap<String, Object> map = logService.getLogByCondition(sql,para,kwIsNotNull,pageSize);
		ObjectMapper mapper = new ObjectMapper();
		String result = mapper.writeValueAsString(map);
		pool.execute(new MyThread(req.getSession(), logService,sql,para,kwIsNotNull));
		return result;
	}
	
	
	
	//翻页功能
	@RequestMapping(value="/getPage")
	public @ResponseBody List<Logs> getPage(int currentPage,int pageSize,HttpServletRequest req) throws Exception{		
		System.out.println("当前页数：" + currentPage);
		System.out.println("当前页条目数：" + pageSize);
		
		//ServletContext servletContext = req.getSession().getServletContext();
		List<All_log>  li = (List<All_log>)req.getSession().getAttribute("li");
			
		Pagination pt = new Pagination();
		if(li != null) {
			pt.setCurrentPage(currentPage);
			pt.setTotalCount(li.size());
			pt.setPaginalCount(pageSize);
		}
		
		
		for (int i=pt.getCurrentPageBegan();i<=pt.getCurrentPageEnd();i++) {
			if(i<li.size()) {
				pt.getList().add(li.get(i));
			}else {
				break;
			}			
		}		
		List<Logs> list = new ArrayList<Logs>();
		for(All_log eachlog:pt.getList()){
			Logs ls = new Logs();
			ls.setContent(eachlog.getContent());
			ls.setId(eachlog.getId());
			ls.setSource_name(eachlog.getLs().getSource_name());
			ls.setLog_type(eachlog.getLog_type());
			ls.setOperator(eachlog.getOperator());
			
			SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //设置格式
			String timeText=format.format(new Date(eachlog.getLog_date()));   
			
			ls.setLog_date(timeText);
			ls.setRemarks(eachlog.getRemarks());
			ls.setTotalC(li.size());
			list.add(ls);			
		}
		return list;
	}	
	//通过id删除日志 
	//required = true 表示不传值则抛出异常
	@RequestMapping(value="/deleteLogData")
	public  @ResponseBody String deleteData(@RequestParam(value="arrid[]", required = false) int[] arrid,HttpServletRequest req) {
		logService.DeleteData(arrid);		
		return null;
	}
	//日志导出
	@RequestMapping(value="/downloadxls")
	public void downloadxls(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<All_log>  li = (List<All_log>) request.getSession().getAttribute("li");
		//ServletContext servletContext = request.getSession().getServletContext();
		//List<All_log>  li = (List<All_log>)servletContext.getAttribute("li");
		HSSFWorkbook book = new HSSFWorkbook();
		dB2xls.writeDbtoExcel(book,"logsys", "all_log",li,1);
		response.setCharacterEncoding("UTF-8");

		/***
			text/plain   一个纯文本
			text/html   //html文件
			image/jpeg     //一个jpeg格式的图片
			application/octet-stream //进行下载操作,设置浏览器以二进制流解析,不知道下载文件类型
			multipart/form-data     // 表单数据,常用于文件上传	
		***/
		response.setContentType("application/octet-stream");
		//此报文头的值有两种， attachment 和 inline， 分别表示保存 还是 直接显示。
		response.setHeader("content-disposition", "attachment;fileName=" + "log.xls");
		// 获取response字节流
		OutputStream out = response.getOutputStream();
		try {
			book.write(out);
		} finally {
			// 关闭
			if(out != null) out.close();
			if(book != null) book.close();
		}
										
	}
	//查询全部日志
	public String findAll(HttpServletRequest req) {
		List<All_log>  li = logService.getAllLog();
		req.getSession().setAttribute("li", li);			
		return null;
	}
	//日志插入接口
	@RequestMapping(value="/saveLog")
	@ResponseBody
	public String saveLog(HttpServletRequest req,HttpServletResponse res) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		
		try {
			All_log al = new All_log();
			if(req.getParameter("log_type") != null) al.setLog_type(req.getParameter("log_type").replaceAll("\\s", ""));
			if(req.getParameter("operator") != null) al.setOperator(req.getParameter("operator").replaceAll("\\s", ""));
			al.setContent(req.getParameter("content"));
			al.setRemarks(req.getParameter("remarks"));
			al.setLog_date(new Date().getTime());
			if(req.getParameter("source_name") != null) {
				String source_name = req.getParameter("source_name").replaceAll("\\s", "");
				Log_source ls = new Log_source();
				ls.setSource_name(source_name);
				al.setLs(ls);
			}
			logService.saveLog(al);
		} catch (Exception e) {
			ErrorMsg errorMsg = new ErrorMsg();
			errorMsg.message = "插入失败";
			return mapper.writeValueAsString(errorMsg);
		}	
		String msg = mapper.writeValueAsString(new SuccessMsg());
		return msg;
	}
	
	@RequestMapping(value="/getDeleteRate")
	public  @ResponseBody DeleteRate getDeleteRate() {
		DeleteRate dr = logService.getRate();	
		System.out.println(dr);
		return dr;
	}
	
	@RequestMapping(value="/setDeleteRate")
	public @ResponseBody String setDeleteRate(String rate) throws IOException {
		int newRate = Integer.parseInt(rate);
		System.out.println(newRate);
		logService.setRate(newRate);	
		return null;
	}
	
	
	//文件上传，excel存储数据库
	@RequestMapping(value="/upload",method = RequestMethod.POST)
	@ResponseBody	//如果不加@ResponseBody，返回的字符串会当做路径去解析，加上后会以respond的body直接返回
	public String uploadExcel(@RequestParam(value="file",required = false)MultipartFile file,HttpServletRequest request, HttpServletResponse response) {
		//value="file"为上传标签中的name值，required = true表示没接受到值的时候抛出异常
		ReadExcel readExcel = new ReadExcel();
		String info = null;
		List<All_log> logList = null;
		try {			
			logList = readExcel.getExcelInfo(file);
			logService.insertLogList(logList);
			info = readExcel.getErrorInfo();
		} catch (Exception e) {
			e.printStackTrace();
			info = "导入失败：文件格式错误或者Excel内容数据格式不符合上传格式";
		}
		if(info == null )
			info = "上传成功，共导入" + logList.size() + "条数据";
		return info;
	}
	

	
	/***读取项目中的文件***/
	public void printFilePath(HttpServletRequest req) {
		try {
			//1. 使用web.xml中的webAppRootKey的配置获取项目根路径
			System.out.println(System.getProperty("webApp.root"));
			//【推荐】2.getResourceAsStream("") 不写路径事表示默认到了该类包的首页目录下 : 普通项目在/src/下，发布后在/web-inf/classes/
			//2.1获取文件流
			//InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("com/logSys/entity/All_log.xml");
			//2.2获取文件路径字符串
			//this.getClass().getClassLoader().getResource("com/logSys/util/qqyx.txt").getPath().substring(1);
			
			System.out.println(req.getSession().getServletContext().getContextPath()); //获取"/项目名"
			System.out.println(req.getSession().getServletContext().getRealPath("/"));  //获取项目的绝对路径
			
			File file = new File(req.getSession().getServletContext().getRealPath("/"));
		    //String [] fileName = file.list(); //获取该路径下的文件名。
			//【注意】在java文件中的路径，不要直接用/开头写文件路径，不容易确定位置，而应使用上述推荐的几种方法。
		    System.out.println(new File(".").getAbsolutePath() + "============="); // "/"代表java虚拟机（jdk安装目录）所在盘符的根目录  .代表虚拟机当前运行的目录
			
		    //3.【关于静态资源路径】：最好使用相对路径。对于上传的文件，不要直接保存在数据库，应该保存在服务器的位置的磁盘，并将路径保存在数据库。
		    //关于页面中的相对路径：对于浏览器来说，/代表服务器的根路径，即：“域名：端口/”，所以页面引用使用/时，“/项目名”才代表“webapp/。在页面最好使用“.”开头的相对路径。
		    
		    //4.【关于转发和重定向】：路径是由服务器解析的，/代表当前项目的根路径下即webapp/下
		    
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

class MyThread extends Thread {
	
	private HttpSession session;
	private LogService logService;
	private String sql;
	private String[] para;
	private boolean kwIsNotNull;
	
	/**
	 * @param session
	 * @param logService
	 * @param sql
	 * @param para
	 * @param kwIsNotNull
	 */
	public MyThread(HttpSession session, LogService logService, String sql, String[] para, boolean kwIsNotNull) {
		super();
		this.session = session;
		this.logService = logService;
		this.sql = sql;
		this.para = para;
		this.kwIsNotNull = kwIsNotNull;
	}

	@Override
	public void run() {
		System.out.println("线程开启执行");
		List<All_log>  li = logService.getLogByKey(sql,para,kwIsNotNull);
		session.setAttribute("li", li);
		System.out.println("线程结束执行");
	}
		
	
}
