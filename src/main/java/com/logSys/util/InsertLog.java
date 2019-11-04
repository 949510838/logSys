package com.logSys.util;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
/**
 * 
* @Title: InsertLog 插入日志
* @Description: 
* @author: zhang
* @date 2019年10月30日 上午10:30:32
 */
public class InsertLog {
	
	/**
	 * 
	* @Title: insert 
	* @Description: 
	* @author: anthor
	* @date 2019年10月30日 上午10:12:22
	* @param log_type 日志类型：增加 删除 修改 查询
	* @param source_name 日志来源名称
	* @param operator 操作者
	* @param content 日志内容
	* @param remarks 日志备注
	* void  
	* @version
	 */
	public static void insert(String log_type,String source_name,String operator,String content,String remarks) {
		//接口地址
		String url = "http://127.0.0.1:8080/logSys/saveLog";
        
		OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("log_type", log_type)
                .add("source_name", source_name)
                .add("operator", operator)
                .add("content", content)
                .add("remarks", remarks)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            System.out.println(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public static void main(String[] args) {
		insert("删除","来源5", "来源5", "来源5", "来源5");
	}
}
