package com.logSys.entity;

public class All_log implements Comparable<All_log>{
	private int id ;
	private Log_source ls;
	private String log_type;
	private String operator;
	private String content;
	private String remarks;
	private long log_date;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Log_source getLs() {
		return ls;
	}
	public void setLs(Log_source ls) {
		this.ls = ls;
	}
	public String getLog_type() {
		return log_type;
	}
	public void setLog_type(String log_type) {
		this.log_type = log_type;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	public long getLog_date() {
		return log_date;
	}
	public void setLog_date(long log_date) {
		this.log_date = log_date;
	}
	@Override
	public String toString() {
		return "All_log [id=" + id + ", ls=" + ls + ", log_type=" + log_type + ", operator=" + operator + ", content="
				+ content + ", remarks=" + remarks + ", log_date=" + log_date + "]";
	}
	@Override
	public int compareTo(All_log al) {
		long value = al.getLog_date() - this.getLog_date();
		if(value > 0) return 1;
		else if(value < 0) return -1;
		else return 0;
	}
	
	
	
}
