package cn.ws.dubbo.service;

import java.util.List;

import cn.ws.dubbo.pojo.User;

public interface UserService {

	/**
	 * 查询所有的用户数据
	 * 
	 * @return
	 */
	public List<User> queryAll();

}

