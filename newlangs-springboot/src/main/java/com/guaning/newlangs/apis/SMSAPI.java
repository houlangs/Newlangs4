package com.guaning.newlangs.apis;

import cn.hutool.http.HttpUtil;

import java.util.HashMap;

public class SMSAPI {
	private static final String URL = "https://api.smsbao.com/sms";
	
	public static String sms(String username, String APIKEY, String phone, String content) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("u", username);
		paramMap.put("p", APIKEY);
		paramMap.put("m", phone);
		paramMap.put("c", content);
		
		return HttpUtil.get(URL, paramMap);
	}
}
