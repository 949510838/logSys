/**
 * 
 */
package logSys.java;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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
public class testRedis_Json {

	//对象的hash与地址值的关系，hashcode()和toString()，==和equals的比较
	@Test
	public void testHash() {
		Object object = new Object();
		//java中对象的hash值指hashcode值，HashSet、HashTable等比较元素相等使用的hashcode值
		System.out.println(object.hashCode()); //object类的hashcode值是将变量引用的内存地址通过hash算法得到的整数值
		System.out.println(object.toString()); //object的tostring方法打印的是：类名@+引用地址的hashcode的16进制整数
		System.out.println(Integer.toHexString(object.hashCode()));
		
		//String重写了hashcode方法，只要字符串相同，hashcode值相等，而不是根据地址来计算
		System.out.println(new String("aaa").hashCode() + "==========" + "aaa".hashCode());
		//number类型也重写了hashcode方法，hashcode值即实际的数值
		System.out.println(new Integer(2).hashCode());
		
		// == 比较的是两个变量的引用地址值
		System.out.println(2 == new Integer(2)); //8种基本类型比较大小直接比较数值，基本类型和number类型比较时依然比较数值
		System.out.println(new Integer(2) == new Integer(2)); //number类型比较大小比较引用地址
		//如果直接继承Object类的equals()方法，依然比较变量的引用地址（Object的equals方法实际就是使用的==比较）
		//String和Number类型都对equals方法进行了重写，只要值相同即相等
		System.out.println(new Integer(2).equals(new Integer(2)));
		
		//print函数打印的就是对象的toString方法
	}
	
	@Test
	public void testKnow() {
		//java中byte是整型，数值范围为-128~128（最高位当符号为看待），可以用来存储二进制数据，是将一个字节的二进制用十进制数值来表示，一个byte即一个字节 
		for(byte b : "abc".getBytes()) {
			System.out.println(b);
		}
	}
	
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
