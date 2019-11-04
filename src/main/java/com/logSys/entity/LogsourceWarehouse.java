package com.logSys.entity;

import java.util.HashSet;
import java.util.Set;

public class LogsourceWarehouse {
	private int id;
	private String source_name;
	private Set<LogWarehouse>  als = new HashSet<LogWarehouse>();;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}	
	
	public Set<LogWarehouse> getAls() {
		return als;
	}
	public void setAls(Set<LogWarehouse> als) {
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
