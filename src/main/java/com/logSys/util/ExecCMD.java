/**
 * 
 */
package com.logSys.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.io.IOUtils;

/**
* @Title: ExecCMD
* @Description: 
* @author: zhang
* @date 2019年11月4日 下午4:22:04
*/
public class ExecCMD {

	public static void exec(String cmd) { 
		StringBuffer output = new StringBuffer();
		Process p;
		InputStreamReader inputStreamReader = null;
		BufferedReader reader = null;
		try {
			p = Runtime.getRuntime().exec(cmd, null);
			p.waitFor();
			inputStreamReader = new InputStreamReader(p.getInputStream(), "GBK");
			reader = new BufferedReader(inputStreamReader);
			String line = "";
			while ((line = reader.readLine()) != null) {
				output.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(reader);
			IOUtils.closeQuietly(inputStreamReader);
		}
		System.out.println(output.toString());


	}
	
	public static void main(String[] args) {
		exec("ping www.baidu.com");
	}
	

}
