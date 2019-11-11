/**
 * 
 */
package logSys.java;

import java.io.Serializable;

/**
* @Title: User
* @Description: 
* @author: zhang
* @date 2019年11月10日 下午6:57:36
*/
public class User implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5556972092711563952L;
	private String name;
	private int age;
	private String adress;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getAdress() {
		return adress;
	}
	public void setAdress(String adress) {
		this.adress = adress;
	}
	

}
