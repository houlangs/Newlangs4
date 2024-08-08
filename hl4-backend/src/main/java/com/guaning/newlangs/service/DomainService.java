package com.guaning.newlangs.service;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guaning.newlangs.entity.Domain;

public interface DomainService extends IService<Domain> {
	//获取域名信息
	SaResult detail(Long id);
	
	//添加域名
	SaResult add(Domain dto);
	
	//修改域名
	SaResult update(Domain dto);
	
	//删除域名
	SaResult delete(Long id);
	
	//获取域名列表
	SaResult list(int page, int pageSize);
	
	//从DNS平台获取域名列表
	SaResult domainListFromDNS(String dns);
}
