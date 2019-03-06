package com.taotao.search.bean;

import java.util.List;

public class SearchResul {
	
	
	private Long total;
	
	private List<?> list;
	
	

	public SearchResul() {
	}

	public SearchResul(Long total, List<?> list) {
		this.total = total;
		this.list = list;
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public List<?> getList() {
		return list;
	}

	public void setList(List<?> list) {
		this.list = list;
	}
	
	
}
