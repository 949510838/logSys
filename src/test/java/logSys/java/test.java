/**
 * 
 */
package logSys.java;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.logSys.common.result.ErrorMsg;
import com.logSys.common.result.SuccessMsg;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
* @Title: test
* @Description: 
* @author: zhang
* @date 2019年11月10日 下午4:29:45
*/
public class test {

	//对象和json互相转换
	@Test
	public void testObjMapper() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		SuccessMsg success = new SuccessMsg();
		
		//对象装json
		String writeValueAsString = mapper.writeValueAsString(success);
		System.out.println(writeValueAsString);
		
		//json转对象
		System.out.println(mapper.readValue(writeValueAsString, ErrorMsg.class));
		
		//map转json
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("code", 1);
		hashMap.put("message", "error");
		System.out.println(mapper.writeValueAsString(hashMap));
		
		//json转map
		System.out.println(mapper.readValue(writeValueAsString, Map.class));
		
		//list转json
		ArrayList<Object> arrayList = new ArrayList<Object>();
		arrayList.add(1);
		arrayList.add("ok");
		System.out.println(mapper.writeValueAsString(arrayList));
		
		//json的list串转List<Map>或者其它List<T.class>类型
		System.out.println((List<Map>)mapper.readValue("["+writeValueAsString+"]",new TypeReference<List<Map>>() { }));
		
		//List<T.class>转json
		arrayList.clear();
		arrayList.add(new SuccessMsg());
		arrayList.add(new ErrorMsg());
		System.out.println(mapper.writeValueAsString(arrayList));
		
		//Map<key,List>转json 
		hashMap.clear();
		hashMap.put("mapList01", arrayList);
		ArrayList<Object> arrayList2 = new ArrayList<Object>();
		arrayList2.add("zhangsan");
		arrayList2.add("12");
		hashMap.put("mapList02", arrayList2);
		System.out.println(mapper.writeValueAsString(hashMap));
	}
	
	@Test
	public void testRedis() {
		String host = "192.168.56.140";
		int port = 6379;
		String password = "2333";
		Jedis jedis = new Jedis(host,port);
		jedis.auth(password);
	
		//System.out.println(jedis.ping());
		//jedis.set("java","哈哈");
		//System.out.println(jedis.get("java"));
		
		if(jedis.exists("java")) {
			System.out.println(jedis.get("java"));
		}
		
		
		jedis.close();
		
	}
	
	@Test
	public void testRedisPool() {
		String host = "192.168.56.140";
		int port = 6379;
		String password = "2333";
	
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		jedisPoolConfig.setMaxTotal(5);
		jedisPoolConfig.setMaxIdle(1);
		
		JedisPool jedisPool = new JedisPool(jedisPoolConfig,host,port);
		
		Jedis jedis = jedisPool.getResource();
		jedis.auth(password);
		
		if(jedis.exists("java")) {
			//System.out.println(jedis.get("java"));
		}
		
		if(jedis.exists("user:java2")) {
			System.out.println(jedis.hgetAll("user:java2"));
		}else {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("name", "zhang");
			map.put("age","12");
			jedis.hmset("user:java2", map);
		}
		
		
		jedis.close();
	}
}
