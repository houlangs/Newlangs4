package com.guaning.newlangs.apis;

import cn.hutool.http.HttpUtil;

import java.util.HashMap;

public class Pay {
	
	private static final String URL = "https://www.juzfu.cn/mapi.php";
	
	public static String pay(int pid,
	                         String type,
	                         String out_trade_no,
	                         String notify_url,
	                         String name,
	                         String money,
	                         String clientip,
	                         String device,
	                         String sign,
	                         String	sign_type) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("pid", pid);
		paramMap.put("type", type);
		paramMap.put("out_trade_no", out_trade_no);
		paramMap.put("notify_url", notify_url);
		paramMap.put("name", name);
		paramMap.put("money", money);
		paramMap.put("clientip", clientip);
		paramMap.put("device", device);
		paramMap.put("sign", sign);
		paramMap.put("sign_type", sign_type);
		
		return HttpUtil.post(URL, paramMap);
	}
}
