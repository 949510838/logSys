package com.logSys.entity;

public class Logs {
	private int id ;
	private String source_name;
	private String log_type;
	private String operator;
	private String content;
	private String remarks;
	private String log_date;
	private int totalC;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	
	public int getTotalC() {
		return totalC;
	}
	public void setTotalC(int totalC) {
		this.totalC = totalC;
	}
	public String getSource_name() {
		return source_name;
	}
	public void setSource_name(String source_name) {
		this.source_name = source_name;
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
	public String getLog_date() {
		return log_date;
	}
	public void setLog_date(String log_date) {
		this.log_date = log_date;
	}
	@Override
	public String toString() {
		return "Logs [id=" + id + ", source_name=" + source_name + ", log_type=" + log_type + ", operator=" + operator
				+ ", content=" + content + ", remarks=" + remarks + ", log_date=" + log_date + ", totalC=" + totalC
				+ "]";
	}
	

}
