package com.guaning.newlangs.service;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guaning.newlangs.entity.DomainConfig;

public interface DomainConfigService extends IService<DomainConfig> {
	//添加dns配置
	SaResult add(DomainConfig dto);
	
	//更新配置
	SaResult update(DomainConfig dto);
	
	//获取配置详情
	SaResult getOne(String dns);
	
	//配置列表
	SaResult list(int page, int pageSize);
	
	//删除配置
	SaResult delete(String dns);
}
