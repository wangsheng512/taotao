package com.taotao.web.service;


import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.service.ApiService;
import com.taotao.web.bean.User;

@Service
public class UserService {
	
	@Autowired
	private ApiService apiService;
	
	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	@Value("${TAOTAO_SSO_URL}")
	public String TAOTAO_SSO_URL;
	
	/**
	 * 根据token查询用户信息
	 * @param token
	 * @return
	 */
	public User queryByToken(String token){
		
		try {
			String url = TAOTAO_SSO_URL + "/service/user/" + token;
			String jsonData = this.apiService.doGet(url);
			if(StringUtils.isNotEmpty(jsonData)){
				//反序列化
				return MAPPER.readValue(jsonData, User.class);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
