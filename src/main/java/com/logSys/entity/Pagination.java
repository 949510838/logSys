package com.logSys.entity;

import java.util.ArrayList;
import java.util.List;

public class Pagination {
	private int totalPageCount;
	private int totalCount;	
	private int currentPage = 1;
	private int currentPageBegan;
	private int currentPageEnd;
	private int paginalCount = 10;
	private List<All_log> list = new ArrayList<All_log>();
	public int getTotalPageCount() {
		int count = getTotalCount()/paginalCount;
		if(list.size()%paginalCount == 0) {
			
			return count;
		}else{
			return count+1;
		}
		
	}
	public void setTotalPageCount(int totalPageCount) {
		this.totalPageCount = totalPageCount;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public int getCurrentPageBegan() {
		return (getCurrentPage()-1)*getPaginalCount();
	}
	public void setCurrentPageBegan(int currentPageBegan) {
		this.currentPageBegan = currentPageBegan;
	}
	public int getCurrentPageEnd() {
		return getCurrentPageBegan() + getPaginalCount() - 1;
	}
	public void setCurrentPageEnd(int currentPageEnd) {
		this.currentPageEnd = currentPageEnd;
	}
	public int getPaginalCount() {
		return paginalCount;
	}
	public void setPaginalCount(int paginalCount) {
		this.paginalCount = paginalCount;
	}
	public List<All_log> getList() {
		return list;
	}
	public void setList(List<All_log> list) {
		this.list = list;
	}
	
	
}
