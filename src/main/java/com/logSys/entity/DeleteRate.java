package com.logSys.entity;

public class DeleteRate {
	private int id ;
	private int rate;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getRate() {
		return rate;
	}
	public void setRate(int rate) {
		this.rate = rate;
	}
	@Override
	public String toString() {
		return "DeleteRate [id=" + id + ", rate=" + rate + "]";
	}
	
	
	
}
