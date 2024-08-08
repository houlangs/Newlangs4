package com.guaning.newlangs.service.impl;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guaning.newlangs.entity.DomainConfig;
import com.guaning.newlangs.mapper.DomainConfigMapper;
import com.guaning.newlangs.service.DomainConfigService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DomainConfigServiceImpl extends ServiceImpl<DomainConfigMapper, DomainConfig> implements DomainConfigService {
	//添加dns平台配置
	@Override
	public SaResult add(DomainConfig dto) {
		//查询是否存在
		DomainConfig one = getById(dto.getDns());
		if (one != null) {
			return SaResult.error("该DNS已存在，请更换平台");
		}
		
		dto.setCreatedTime(LocalDateTime.now());
		save(dto);
		
		return SaResult.ok("添加成功");
	}
	
	//更新dns平台配置
	@Override
	public SaResult update(DomainConfig dto) {
		//查询是否存在
		DomainConfig one = getById(dto.getDns());
		if (one == null) {
			return SaResult.error("该DNS不存在");
		}
		
		dto.setUpdatedTime(LocalDateTime.now());
		updateById(dto);
		
		return SaResult.ok("更新成功");
	}
	
	//获取DNS平台配置
	@Override
	public SaResult getOne(String dns) {
		//查询是否存在
		DomainConfig one = getById(dns);
		if (one == null) {
			return SaResult.error("该DNS不存在");
		}
		
		return SaResult.data(one);
	}
	
	//dns平台配置列表
	@Override
	public SaResult list(int page, int pageSize) {
		//构造分页构造器
		Page<DomainConfig> domainConfigPage = new Page<>(page, pageSize);
		
		//按创建时间排序
		page(domainConfigPage, Wrappers.<DomainConfig>lambdaQuery().orderByAsc(DomainConfig::getCreatedTime));
		
		return SaResult.data(domainConfigPage);
	}
	
	//删除配置
	@Override
	public SaResult delete(String dns) {
		//查询是否存在
		DomainConfig one = getById(dns);
		if (one == null) {
			SaResult.error("该DNS不存在");
		}
		
		removeById(dns);
		
		return SaResult.ok("删除成功");
	}
}
