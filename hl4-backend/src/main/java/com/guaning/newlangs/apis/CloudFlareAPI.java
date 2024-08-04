package com.guaning.newlangs.apis;

import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSONObject;
import com.guaning.newlangs.dto.DomainRecordCommonDto;
import org.springframework.stereotype.Component;

@Component
public class CloudFlareAPI {
	
	private static final String API = "https://api.cloudflare.com/client/v4";
	
	//获取域名列表
	public String listZones(String email, String key, int page, int perPage) {
		return HttpRequest.get(API + "/zones" + "?page=" + page + "&per_page=" + perPage)
				.header(Header.CONTENT_TYPE, "application/json")
				.header("X-Auth-Email", email)
				.header("X-Auth-Key", key)
				.timeout(30000)
				.execute().body();
	}
	
	//获取域名记录列表
	public String listRecords(String email, String key, String zoneIdentifier, int page, int perPage) {
		return HttpRequest.get(API + "/zones/" + zoneIdentifier + "/dns_records" + "?page=" + page + "&per_page=" + perPage)
				.header(Header.CONTENT_TYPE, "application/json")
				.header("X-Auth-Email", email)
				.header("X-Auth-Key", key)
				.timeout(30000)
				.execute().body();
	}
	
	//获取域名记录详情
	public String recordDetail(String email, String key, String zoneIdentifier, String identifier) {
		return HttpRequest.get(API + "/zones/" + zoneIdentifier + "/dns_records/" + identifier)
				.header(Header.CONTENT_TYPE, "application/json")
				.header("X-Auth-Email", email)
				.header("X-Auth-Key", key)
				.timeout(30000)
				.execute().body();
	}
	
	//添加域名记录
	public String addRecord(DomainRecordCommonDto dto) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("content", dto.getValue());
		jsonObject.put("name", dto.getPrefix());
		jsonObject.put("proxied", dto.getLineId() == 1);
		jsonObject.put("type", dto.getType());
		jsonObject.put("comment", dto.getComment());
		
		return HttpRequest.post(API + "/zones/" + dto.getDomainId() + "/dns_records/")
				.header(Header.CONTENT_TYPE, "application/json")
				.header("X-Auth-Email", dto.getEmail())
				.header("X-Auth-Key", dto.getKey())
				.body(String.valueOf(jsonObject))
				.timeout(30000)
				.execute().body();
	}
	
	//删除域名记录
	public String deleteRecord(DomainRecordCommonDto dto) {
		return HttpRequest.delete(API + "/zones/" + dto.getDomainId() + "/dns_records/" + dto.getRecordId())
				.header(Header.CONTENT_TYPE, "application/json")
				.header("X-Auth-Email", dto.getEmail())
				.header("X-Auth-Key", dto.getKey())
				.timeout(30000)
				.execute().body();
	}
	
	//更新域名记录
	public String updateRecord(DomainRecordCommonDto dto) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("content", dto.getValue());
		jsonObject.put("name", dto.getPrefix());
		jsonObject.put("proxied", dto.getLineId() == 1);
		jsonObject.put("type", dto.getType());
		jsonObject.put("comment", dto.getComment());
		
		return HttpRequest.put(API + "/zones/" + dto.getDomainId() + "/dns_records/" + dto.getRecordId())
				.header(Header.CONTENT_TYPE, "application/json")
				.header("X-Auth-Email", dto.getEmail())
				.header("X-Auth-Key", dto.getKey())
				.timeout(30000)
				.body(String.valueOf(jsonObject))
				.execute().body();
	}
}
