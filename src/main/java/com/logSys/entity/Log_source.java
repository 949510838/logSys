package com.logSys.entity;

import java.util.HashSet;
import java.util.Set;

public class Log_source {
	private int id;
	private String source_name;
	private Set<All_log>  als = new HashSet<All_log>();;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Set<All_log> getAls() {
		return als;
	}
	public void setAls(Set<All_log> als) {
		this.als = als;
	}
	
	public String getSource_name() {
		return source_name;
	}
	public void setSource_name(String source_name) {
		this.source_name = source_name;
	}
	@Override
	public String toString() {
		return "Log_source [id=" + id + ", source_name=" + source_name + "]";
	}
	
	
	
}
