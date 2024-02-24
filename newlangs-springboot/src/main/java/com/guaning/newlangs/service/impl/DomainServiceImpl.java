package com.guaning.newlangs.service.impl;

import cn.dev33.satoken.util.SaResult;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guaning.newlangs.apis.CloudFlareAPI;
import com.guaning.newlangs.entity.Domain;
import com.guaning.newlangs.entity.DomainRecord;
import com.guaning.newlangs.mapper.DomainMapper;
import com.guaning.newlangs.service.DomainConfigService;
import com.guaning.newlangs.service.DomainRecordService;
import com.guaning.newlangs.service.DomainService;
import com.guaning.newlangs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class DomainServiceImpl extends ServiceImpl<DomainMapper, Domain> implements DomainService {
	private final CloudFlareAPI cf;
	private final DomainConfigService domainConfigService;
	
	public DomainServiceImpl(CloudFlareAPI cf, DomainConfigService domainConfigService) {
		this.cf = cf;
		this.domainConfigService = domainConfigService;
	}
	
	private DomainRecordService domainRecordService;
	
	@Autowired
	public void setDomainRecordService(DomainRecordService domainRecordService) {
		this.domainRecordService = domainRecordService;
	}
	
	//获取域名信息
	@Override
	public SaResult detail(Long id) {
		//查询域名是否存在
		Domain domainOne = getById(id);
		if (domainOne == null) {
			return SaResult.error("域名不存在");
		}
		
		return SaResult.data(domainOne);
	}
	
	//添加域名
	@Override
	public SaResult add(Domain dto) {
		//查询域名是否存在
		Domain domainOne = getOne(Wrappers.<Domain>lambdaQuery().eq(Domain::getName, dto.getName()));
		if (domainOne != null) {
			return SaResult.error("域名已存在");
		}
		
		Domain domain = new Domain();
		domain.setDomainId(dto.getDomainId());
		domain.setDns(dto.getDns());
		domain.setName(dto.getName());
		domain.setRoleIds(dto.getRoleIds());
		domain.setPoint(dto.getPoint());
		domain.setComment(dto.getComment());
		domain.setCreatedTime(LocalDateTime.now());
		domain.setUpdatedTime(LocalDateTime.now());
		save(domain);
		
		return SaResult.ok("添加成功");
	}
	
	
	//更新域名信息
	@Override
	public SaResult update(Domain dto) {
		//查询域名是否存在
		Domain domainOne = getOne(Wrappers.<Domain>lambdaQuery().eq(Domain::getId, dto.getId()));
		if (domainOne == null) {
			return SaResult.error("域名不存在");
		}
		
		Domain domain = new Domain();
		domain.setId(dto.getId());
		domain.setRoleIds(dto.getRoleIds());
		domain.setPoint(dto.getPoint());
		domain.setComment(dto.getComment());
		domain.setUpdatedTime(LocalDateTime.now());
		updateById(domain);
		
		return SaResult.ok("更新成功");
	}
	
	//删除域名
	@Override
	public SaResult delete(Long id) {
		//查询域名是否存在
		Domain domainOne = getById(id);
		if (domainOne == null) {
			return SaResult.error("域名不存在");
		}
		
		//根据id查询域名记录
		DomainRecord record = domainRecordService.getOne(Wrappers.<DomainRecord>lambdaQuery().eq(DomainRecord::getDid, domainOne.getId()));
		if (record != null) {
			//域名记录id
			Long recordId = record.getId();
			domainRecordService.removeById(recordId);
			removeById(id);
		} else {
			removeById(id);
		}
		
		return SaResult.ok("删除成功");
	}
	
	//从数据库获取域名列表
	@Override
	public SaResult list(int page, int pageSize) {
		//构造分页构造器
		Page<Domain> domainPage = new Page<>(page, pageSize);
		
		//按创建时间排序
		page(domainPage, Wrappers.<Domain>lambdaQuery().orderByAsc(Domain::getCreatedTime));
		
		return SaResult.data(domainPage);
	}
	
	//从DNS平台获取域名列表
	@Override
	public SaResult domainListFromDNS(String dns) {
		//获取DNS配置
		JSONObject data = JSON.parseObject(JSON.toJSONString(domainConfigService.getOne(dns).getData()));
		JSONObject config = JSON.parseObject(data.getString("config"));
		String email = config.getString("email");
		String key = config.getString("key");
		
		JSONArray domainList = new JSONArray();
		switch (dns) {
			case "CloudFlare" -> {
				//调用CloudFlare HTTP API，接口返回信息进行处理
				JSONArray result = JSON.parseObject(cf.listZones(email, key, 1, 1000)).getJSONArray("result");
				if (result == null) {
					return SaResult.error("网络不佳，请多试几次");
				} else {
					for (int i = 0; i < result.size(); i++) {
						JSONObject domainMap = new JSONObject();
						domainMap.put("domainId", JSON.parseObject(result.getString(i)).getString("id"));
						domainMap.put("name", JSON.parseObject(result.getString(i)).getString("name"));
						domainList.add(domainMap);
					}
				}
			}
			case "DnsPod" -> {
			}
				/*
				  待加
				 */
			case "AliYun" -> {
			}
				/*
				  待加
				 */
		}
		
		return SaResult.data(domainList);
	}
}
