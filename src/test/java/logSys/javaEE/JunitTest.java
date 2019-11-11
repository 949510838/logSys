/**
 * 
 */
package logSys.javaEE;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.logSys.controller.LogController;
import com.logSys.dao.LogDao;
import com.logSys.service.LogService;

import redis.clients.jedis.Jedis;

/**
* @Title: JunitTest
* @Description: 
* @author: zhang
* @date 2019年11月10日 下午3:04:57
*/
public class JunitTest extends BaseJunit4Test {
	
	@Resource
	private LogService logService;
	
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
	
	@Test
	public void testRedis02() {
		ValueOperations<String, String> res = redisTemplate.opsForValue();
		
		//设置过期时间，第二个参数为时间，TimeUnit可以设置单位为s m h day
		redisTemplate.expire("user", 20, TimeUnit.SECONDS);
		
		if(redisTemplate.hasKey("user:java2")) {
			System.out.println(res);
		}else {
			System.out.println("not exists");
			res.set("user:java3","test");
		}
	}
	
	

	
}
