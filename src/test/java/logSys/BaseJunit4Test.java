/**
 * 
 */
package logSys;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.logSys.controller.LogController;
import com.logSys.service.LogService;

/**
* @Title: BaseJunit4Test
* @Description: junit测试基类，子类继承后不用再写@RunWith和@ContextConfiguration配置
* @author: zhang
* @date 2019年11月10日 下午3:01:06
*/

@RunWith(SpringJUnit4ClassRunner.class) //使用junit4进行测试  
@ContextConfiguration(locations = {"classpath:springmvc.xml"}) //加载配置文件，用于在测试类中注入IOC中的对象
public class BaseJunit4Test {

}
