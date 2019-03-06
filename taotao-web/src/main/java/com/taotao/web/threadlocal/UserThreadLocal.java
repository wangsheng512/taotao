package com.taotao.web.threadlocal;

import com.taotao.web.bean.User;

public class UserThreadLocal {
	
	//静态不能改变
	private static final ThreadLocal<User> LOCAl = new ThreadLocal<User>();
	
	private UserThreadLocal(){
		
	}
	
	public static void set(User user){
		LOCAl.set(user);
	}
	
	public static User get(){
		return LOCAl.get();
	}
}
