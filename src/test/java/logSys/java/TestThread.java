/**
 * 
 */
package logSys.java;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Test;

/**
* @Title: test
* @Description: 
* @author: zhang
* @date 2019年12月7日 下午4:30:51
*/
public class TestThread {

	public synchronized void t1() throws Exception {
		Thread.sleep(5000);
		System.out.println("t1");
	}
	
	public synchronized void t2() throws Exception {
		Thread.sleep(3000);
		System.out.println("t2");
	}
	//新建的线程会随测试方法主线程结束而结束，所以需要将主线程睡眠
	@Test
	public void test() throws Exception {
		TestThread tt = new TestThread();
		System.out.println(123);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					tt.t1();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}).start();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					tt.t2();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}).start();
		Thread.sleep(10000);
	}
	
	public static void main(String[] args) {
		TestThread tt = new TestThread();
		System.out.println(123);
		new Thread(()->{
			try {
				tt.t1();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}).start();
		new Thread(()-> {			
			try {
				tt.t2();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}				
		}).start();
	}
	@Test
	public void test01() {
		System.out.println(new TestThread().getClass()==TestThread.class);
		System.out.println(new TestThread().getClass().equals(TestThread.class));
	}
	@Test
	public void test02() {
		ExecutorService service = Executors.newFixedThreadPool(5);
		System.out.println(service.toString());
		System.out.println(Runtime.getRuntime().availableProcessors());
		
	}
}
