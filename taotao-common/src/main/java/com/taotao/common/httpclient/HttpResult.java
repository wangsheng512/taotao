package com.taotao.common.httpclient;

public class HttpResult {
	
	private Integer code;
	
	private String body;
	
	

	public HttpResult() {
	}

	public HttpResult(Integer code, String body) {
		this.code = code;
		this.body = body;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}
	
	
}
