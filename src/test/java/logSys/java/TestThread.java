/**
 * 
 */
package logSys.java;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.TreeMap;
import java.util.TreeSet;
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
	
	public static void main(String[] args) throws InterruptedException {
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
	@Test
	public void test03() throws Exception {
		Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				System.out.println(Thread.currentThread());
				
			}
		});
		thread.start();
		System.out.println(thread);
		thread.sleep(1000);
		new String();
		"".concat("");
		new StringBuilder("");
		int a = 0x11;
		System.out.println(a);
		
		
		
	}
	@Test
	public void test04() {
		TreeSet tr = new TreeSet();
		tr.add("a");
		tr.add("c");
		tr.add("b");
		System.out.println(tr);
		TreeMap tm = new TreeMap();
		tm.put("a", 1);
		tm.put("c", 2);
		tm.put("b", 3);
		System.out.println(tm);
		
		String a = new File("") + "";
		a = new File("/") + new String("aaa");
		a = new Integer(2) + "/";
		System.out.println(a);
		System.out.println("张".getBytes().length);
		System.out.println(new BigInteger("1111111111111111111111111111111111111111111111111111111111").longValue());
	}
	
	@Test
	public void testJDBC() throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
	}
}
