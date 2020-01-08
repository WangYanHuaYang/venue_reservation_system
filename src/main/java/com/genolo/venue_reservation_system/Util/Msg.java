package com.genolo.venue_reservation_system.Util;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 通用的返回的类
 * 
 * @author wyhy
 * 
 */
@Data
public class Msg {

	private int Code;

	private String Message;

	private Map<String, Object> Context;

	public static Msg SUCCESS(){
		Msg result=new Msg();
		result.setCode(0);
		result.setMessage("请求成功");
		return result;
	}
	public static Msg CUSTOM_MSG(String msg){
		Msg result=new Msg();
		result.setCode(100);
		result.setMessage(msg);
		return result;
	}
	public static Msg CUSTOM_MSG(int code,String msg){
		Msg result=new Msg();
		result.setCode(code);
		result.setMessage(msg);
		return result;
	}

	public static Msg URL_ERROR(){
		Msg result=new Msg();
		result.setCode(400);
		result.setMessage("请求在语法上不正确");
		return result;
	}

	public static Msg FAIL(){
		Msg result=new Msg();
		result.setCode(404);
		result.setMessage("请求失败");
		return result;
	}

	public static Msg NO_ACCESS(){
		Msg result=new Msg();
		result.setCode(403);
		result.setMessage("请求无权限");
		return result;
	}

	public static Msg NO_LOGIN(){
		Msg result=new Msg();
		result.setCode(407);
		result.setMessage("请先登录后再请求");
		return result;
	}

	public Msg add(String key, Object value) {
		this.Context=new HashMap<>();
		this.Context.put(key, value);
		return this;
	}

}
