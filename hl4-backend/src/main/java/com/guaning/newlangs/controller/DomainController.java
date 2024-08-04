package com.guaning.newlangs.controller;

import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.util.StrUtil;
import com.guaning.newlangs.entity.Domain;
import com.guaning.newlangs.service.DomainConfigService;
import com.guaning.newlangs.service.DomainService;
import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/domain")
public class DomainController {
	private DomainService domainService;
	
	@Autowired
	public void setDomainService(DomainService domainService) {
		this.domainService = domainService;
	}
	
	//获取域名信息
	@GetMapping("/detail/{id}")
	public SaResult detail(@PathVariable(value = "id") Long id) {
		if (id == null) {
			return SaResult.error("未获取到有效参数");
		}
		
		return domainService.detail(id);
	}
	
	//添加域名
	@PostMapping("/add")
	public SaResult add(@RequestBody @NotNull Domain dto) {
		if (StrUtil.hasBlank(dto.getDns())) {
			return SaResult.error("DNS解析平台不能为空");
		} else if (StrUtil.hasBlank(dto.getDomainId())) {
			return SaResult.error("域名ID不能为空");
		} else if (dto.getRoleIds() == null) {
			return SaResult.error("域名所属角色不能为空");
		} else if (dto.getPoint() == null) {
			return SaResult.error("域名消耗积分不能为空");
		} else if (StrUtil.hasBlank(dto.getComment())) {
			return SaResult.error("域名介绍不能为空");
		}
		
		return domainService.add(dto);
	}
	
	//更新域名信息
	@PutMapping("/update")
	public SaResult update(@RequestBody @NotNull Domain dto) {
		if (dto.getId() == null) {
			return SaResult.error("未获取到有效参数");
		} else if (dto.getRoleIds() == null) {
			return SaResult.error("域名所属角色不能为空");
		} else if (dto.getPoint() == null) {
			return SaResult.error("域名消耗积分不能为空");
		} else if (StrUtil.hasBlank(dto.getComment())) {
			return SaResult.error("域名介绍不能为空");
		}
		
		return domainService.update(dto);
	}
	
	//删除域名
	@DeleteMapping("/delete/{id}")
	public SaResult delete(@PathVariable("id") Long id) {
		if (id == null) {
			return SaResult.error("未获取到有效参数");
		}
		
		return domainService.delete(id);
	}
	
	//从数据库获取域名列表
	@GetMapping("/list")
	public SaResult list(@RequestParam(required = false, defaultValue = "1", value = "page") int page,
	                         @RequestParam(required = false, defaultValue = "10", value = "pageSize") int pageSize) {
		return domainService.list(page, pageSize);
	}
	
	//从DNS平台获取域名列表
	@GetMapping("/list_from_dns")
	public SaResult domainListFromDNS(String dns) {
		if (StrUtil.hasBlank(dns)) {
			return SaResult.error("DNS解析平台不能为空");
		}
		
		return domainService.domainListFromDNS(dns);
	}
}
