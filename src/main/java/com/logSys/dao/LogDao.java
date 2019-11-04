package com.logSys.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.logSys.entity.All_log;
import com.logSys.entity.DeleteRate;
import com.logSys.entity.LogWarehouse;
import com.logSys.entity.Log_source;
import com.logSys.entity.LogsourceWarehouse;

public class LogDao {
	private SessionFactory sessionFactory;
	private Session session ;
	
	public Session getSession() {
		// 从当前的线程中获取session对象，同一个线程获取到的session对象是同一个
		session = sessionFactory.getCurrentSession();
		return session;
	}
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	public List<All_log> findAllLog() {
		List<All_log> list = (List<All_log>) getSession().createQuery("from All_log").list();	
		Collections.sort(list);
		return list;	
	}
	
	public List<All_log> findByCon(String sql) {
		List<All_log> list = (List<All_log>) getSession().createQuery(sql).list();			
		return list;	
	}
	//按条件查询日志
	public List<All_log> findByKey(String sql,String[] para, boolean kwIsNotNull) {		
		SQLQuery query = getSession().createSQLQuery(sql);
		if(para != null&&para.length != 0) {
			if(kwIsNotNull) { 
				int i = 0;
				for(;i < para.length-1 ;i++) {
					query.setParameter(i, para[i]);	//参数下标从0开始
				}
				query.setParameter(i, "%"+ para[i] + "%").setParameter(i+1, "%"+ para[i] + "%").setParameter(i+2, "%"+ para[i] + "%").setParameter(i+3, "%"+ para[i] + "%").setParameter(i+4, "%"+ para[i] + "%");
			} else {				
				for(int i = 0;i < para.length ;i++) {
					query.setParameter(i, para[i]);
				}
			}
			
		}
		List list = query.list();
		List<All_log> li = new ArrayList<All_log>();
		for(int i=0;i<list.size();i++) {
			Object[] obj = (Object[]) list.get(i);
			All_log al = new All_log();
			al.setId((int)obj[0]);
			Log_source ls = new Log_source();
			ls.setId((int)obj[1]);
			ls.setSource_name((String)obj[2]);
			al.setLs(ls);
			al.setLog_type((String)obj[3]);
			al.setOperator((String)obj[4]);
			al.setContent((String)obj[5]);
			al.setRemarks((String)obj[6]);
			al.setLog_date(Long.valueOf(obj[7].toString()));
			li.add(al);
		}
		Collections.sort(li);
		return li;	
	}
	public void deleteById(int[] arrid) {
		for (int i = 0;i<arrid.length;i++) {
			All_log al = new All_log();
			al.setId(arrid[i]);
			getSession().delete(al);
		} 
	}
	//批量插入数据，先查再插入
	public void save(All_log al) {
		String source_name = al.getLs().getSource_name();
		String hql = "from Log_source where source_name='"+ source_name +"'";
		List list = getSession().createQuery(hql).list();
		if(list.size() == 0) {			
			getSession().save(al.getLs());
		}else {
			Log_source ls = (Log_source)list.get(0);
			al.setLs(ls);
		}		
		getSession().saveOrUpdate(al);
	}
	//关闭自动删除
	public DeleteRate getRate() {
		String sql = "from DeleteRate";
		List list = getSession().createQuery(sql).list();		
		if(list.size() == 0) {
			DeleteRate dr = new DeleteRate();
			dr.setId(0);
			return dr;
		}		
			
		return (DeleteRate) list.get(0);
	}
	//自动删除速率设置
	public void setRate(int rate) {
		String sql = "from DeleteRate";
		List list = getSession().createQuery(sql).list();				
		if(list.size() == 0) {
			DeleteRate dr = new DeleteRate();
			dr.setRate(rate);
			//saveOrUpdate方法要求ID为null（如果id是int类型，则是0）时才执行SAVE，在其它情况下执行UPDATE
			//在保存实例的时候是新增,当ID不为null，所以使用的是UPDATE，但是数据库里没有主键相关的值，所以出现异常。
			getSession().saveOrUpdate(dr);
		}else {
			DeleteRate dr  = (DeleteRate) list.get(0);
			dr.setRate(rate);
			getSession().save(dr);
		}
		
		
	}
	//批量插入数据，不能采用非批量的方法，因为有缓存的机制。
	public void deleteTask() {
		String sql = "from DeleteRate";
		Session session = this.getSession();
		List list = session.createQuery(sql).list();
		if(list.size() != 0) {
			int rate  = ((DeleteRate) list.get(0)).getRate();
			
			if(rate > 0) {
				
				List<All_log> loglist = (List<All_log>) session.createQuery("from All_log").list();	
				//System.out.println(loglist);
				System.out.println("================开始遍历===================");
				List<All_log> deletelist = new ArrayList<All_log>() ;
				List<LogWarehouse> insertlist = new ArrayList<LogWarehouse>() ;
		
				session.clear();
				for (All_log al :loglist) {
					long a = new Date().getTime()-al.getLog_date();
					long b = ((long)rate)*30l*24l*60l*60l*1000l;					
					if(a >= b) {						
						LogWarehouse lw = new LogWarehouse();
						lw.setContent(al.getContent());
						lw.setLog_date(al.getLog_date());
						lw.setLog_type(al.getLog_type());
						lw.setOperator(al.getOperator());
						lw.setRemarks(al.getRemarks());
						
						LogsourceWarehouse ls = new LogsourceWarehouse();
						ls.setSource_name(al.getLs().getSource_name());
						lw.setLs(ls);
												
						insertlist.add(lw);
						deletelist.add(al);	
					}
				}
				int i = 0;
				//批量插入
				for (LogWarehouse lw : insertlist) {
					System.out.println("================开始插入===================" + i++);
			        int id = lw.getLs().getId();
					List<LogsourceWarehouse> li = (List<LogsourceWarehouse> )session.createQuery("from LogsourceWarehouse where source_name='" + lw.getLs().getSource_name() +"'").list();
			        //本地sql查询不会产生对象bean的一级缓存
			        //List<Object[]> li = (List<Object[]>)session.createSQLQuery("select * from log_source_warehouse where source_name='" + lw.getLs().getSource_name() + "'").list();
					if(li.size() == 0) {
						//session.flush();  //可以立即同步持久化状态数据到数据库表
						//session.clear();  //可以清除一级缓存
						session.save(lw.getLs());						
					}else {
						lw.setLs(li.get(0));			
					}
					//session.flush();
					//session.clear();
					session.saveOrUpdate(lw);
				}
				i = 0;
				//批量删除
				for (All_log al : deletelist) {
					System.out.println("================开始删除===================" + i++);
					session.delete(al);
				}
	
			}
		}

	}
} 
