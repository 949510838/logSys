/**
 * 
 */
package logSys.javaEE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.logSys.common.result.SuccessMsg;
import com.logSys.controller.LogController;
import com.logSys.dao.LogDao;
import com.logSys.service.LogService;

import logSys.java.User;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

/**
* @Title: JunitTest
* @Description: 
* @author: zhang
* @date 2019年11月10日 下午3:04:57
*/
public class JunitTestRedis extends BaseJunit4Test {
	
	@Resource
	private LogService logService;
	@Resource
	private JedisConnectionFactory factory; //连接工厂对象
	@Resource
	private JedisPool jedisPool;	//jedis连接池对象
	
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	public RedisTemplate<String, String> getRedisTemplate() {
		//解决乱码
		//设置序列化Key的实例化对象
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        //设置序列化Value的实例化对象
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
		return redisTemplate;
	}

	@Test
	public void test() {
		System.out.println(logService);
		
	}
	
	/* 通过redis连接工厂类拿到Jedis对象 */
	@Test
	public void testRedis01() throws InterruptedException {
		//获取Jedis对象
		Jedis jedis = (Jedis) factory.getConnection().getNativeConnection();
		
		jedis.set("jedis","test");
		System.out.println(jedis.get("jedis"));
		
		List<Object> list = null;
		while(list == null) {
			jedis.watch("k1");
			Transaction ts = jedis.multi();
			System.out.println("======================");
			Thread.sleep(10000);
			ts.set("k1", "11");
			list = ts.exec();  //成功返回["ok"],失败返回null
			System.out.println(list);
		}
		
		
		jedis.close();
	}
	
	/* 直接使用jedisPool连接池对象获取jedis连接对象 */
	@Test
	public void testRedis02() {
		 // 获取jedis连接
	    Jedis jedis = jedisPool.getResource();
	    String name = jedis.get("k1");
	    System.out.println(name);
	    // 归还连接
	    jedis.close();
	}
	
/**************************************** jedisTemplate操作***********************************/
	
	/* 通过jedisTemplate操作redis */
	@Test
	public void testRedis() {
		ValueOperations<String, String> res = getRedisTemplate().opsForValue();
		
		//设置过期时间，第二个参数为时间，TimeUnit可以设置单位为s m h day
		getRedisTemplate().expire("user", 20, TimeUnit.SECONDS);
		
		if(getRedisTemplate().hasKey("user:java2")) {
			System.out.println(res);
		}else {
			System.out.println("not exists");
			res.set("user:java3","test");
		}
	}
	
	/* 字符串类型的操作 */ 
	@Test
	public void testRedisString() {
		ValueOperations<String, String> res = redisTemplate.opsForValue();
		
		//设置过期时间，第二个参数为时间，TimeUnit可以设置单位为s m h day
		redisTemplate.expire("user", 20, TimeUnit.SECONDS);
		
		if(redisTemplate.hasKey("user:String")) {
			System.out.println(res.get("user:String"));
		}else {
			System.out.println("not exists");
			res.set("user:String","test");
		}
	}
	
	
		
	/* hash类型的操作 */
	@Test
	public void testRedisHash() {
		HashOperations<String, Object, Object> res = redisTemplate.opsForHash();
		
		User user = new User();
		user.setId("123");
		user.setName("张三");
		res.put("user:hash", "bbb", "123");
		if(res.hasKey("user:hash",user.getId())) {
			System.out.println(res.size("user:hash"));
			//System.out.println(res.values("user:hash"));
			System.out.println(res.get("user:hash", "ccc"));
			System.out.println(res.get("user:hash", user.getId()));
		}else {
			System.out.println("not exists");
			
			//res.put("user:hash",user.getId() , user);
			/*
			 * HashMap map = new HashMap(); map.put("name", "zhang"); map.put("age",13);
			 * res.put("user:hash", user, map);
			 */
			
		}
	}
	
	@Test
	public void testRedisList() {
		ListOperations<String, String> res = redisTemplate.opsForList();
		
		res.leftPush("user:list", "zhang");
		res.leftPush("user:list", "12");
		System.out.println(res.size("user:list"));
		ArrayList<String> arrayList = new ArrayList<String>();
		arrayList.add("aaaa");
		res.leftPushAll("user:list", arrayList);
		System.out.println(res.range("user:list", 0, 1));
		
	}
	
	
	

	
}
