/**
 * 
 */
package logSys;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.logSys.controller.LogController;
import com.logSys.dao.LogDao;
import com.logSys.service.LogService;

/**
* @Title: JunitTest
* @Description: 
* @author: zhang
* @date 2019年11月10日 下午3:04:57
*/
public class JunitTest extends BaseJunit4Test {
	
	@Resource
	private LogService logService;
	
	
	@Test
	public void test() {
		System.out.println("aaa");
		System.out.println(logService);
		
	}
}
