package com.logSys.filter;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.springframework.web.filter.OncePerRequestFilter;

public class HandleCharacter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		System.out.println("==========================filter========================");
		
		//前后端分离，支持跨域访问的设置
		//setCORS(response);

        
		/*
		  *  解决post提交乱码问题 
		 * request.setCharacterEncoding("UTF-8"); // POST提交有效
		 * response.setContentType("text/html;charset=UTF-8");
		 */
		final HttpServletRequest req = request;
		//处理get提交乱码（tomcat服务器专用）
        //获取HttpServletRequest对象的代理对象
		HttpServletRequest requestProxy =  (HttpServletRequest) Proxy.newProxyInstance(
				request.getClass().getClassLoader(), 		// 指定当前使用的累加载器
				new Class[]{HttpServletRequest.class}, 		// 对目标对象实现的接口类型
				new InvocationHandler() {					// 事件处理器，每次调用request的方法都会进行该事件处理器
					@Override
					public Object invoke(Object proxy, Method method, Object[] args) //args：该方法的参数
							throws Throwable {
						// 定义方法返回值
						Object returnValue = null;
						// 获取方法名
						String methodName = method.getName();
						// 判断：对getParameter方法进行GET提交中文处理
						if ("getParameter".equals(methodName)) {
							
							// 获取请求数据值【 <input type="text" name="userName">】
							String value = req.getParameter(args[0].toString());	// 调用目标对象的方法
							
							// 获取提交方式
							String methodSubmit = req.getMethod(); // 直接调用目标对象的方法
							
							// 判断如果是GET提交，需要对数据进行处理  (POST提交已经处理过了)
							if ("GET".equals(methodSubmit)) {
								if (value != null && !"".equals(value.trim())){
									// 处理GET中文
									value = new String(value.getBytes("ISO8859-1"),"UTF-8");
								}
							} 
							System.out.println(value); 
							return value;
						}
						else {
							// 执行request对象的其他方法
							returnValue = method.invoke(req, args);
						}	
						return returnValue;
					}
				});
        
        /**
         	* 传入代理对象requestProxy给doFilter方法
         	* 这样用户在使用request对象时实际上使用的是HttpServletRequest对象的代理对象requestProxy
         */
		filterChain.doFilter(requestProxy, response);

	}
	
	//前后端分离，支持跨域访问的设置
	private void setCORS(HttpServletResponse response) {
		//一般设置下面主要的三项即可实现跨域
		//允许跨域的主机地址 ,服务端设置 Access-Control-Allow-Origin 就可以开启 CORS通信，CORS即跨源资源共享， 该属性表示哪些域名可以访问资源
		response.setHeader("Access-Control-Allow-Origin", "*"); 
		// 允许跨域的请求方法GET, POST, HEAD 等 
		response.setHeader("Access-Control-Allow-Methods","POST, PUT, GET, OPTIONS, DELETE");
		//允许跨域的请求头 
		response.setHeader("Access-Control-Allow-Headers","Origin, X-Requested-With, Content-Type, Accept, client_id, uuid, Authorization"); 
		
		//重新预检验跨域的缓存时间 (s) 
		response.setHeader("Access-Control-Max-Age", "3600"); //设置过期时间
		response.setHeader("Cache-Control","no-cache, no-store, must-revalidate"); // 支持HTTP 1.1.
		response.setHeader("Pragma", "no-cache"); // 支持HTTP 1.0.
		response.setHeader("Expires", "0");
		//是否携带cookie 
		response.setHeader("Access-Control-Allow-Credentials", "true");  
	}

}
