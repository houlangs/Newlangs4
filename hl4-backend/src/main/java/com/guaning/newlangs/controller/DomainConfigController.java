package com.guaning.newlangs.controller;

import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.util.StrUtil;
import com.guaning.newlangs.entity.DomainConfig;
import com.guaning.newlangs.service.DomainConfigService;
import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/domain_config")
public class DomainConfigController {
	private DomainConfigService domainConfigService;
	
	@Autowired
	public void setDomainConfigService(DomainConfigService domainConfigService) {
		this.domainConfigService = domainConfigService;
	}
	
	//添加DNS平台配置
	@PostMapping("/add")
	public SaResult add(@RequestBody @NotNull DomainConfig dto) {
		if (StrUtil.hasBlank(dto.getDns())) {
			return SaResult.error("DNS不能为空");
		} else if (StrUtil.hasBlank(dto.getConfig())) {
			return SaResult.error("配置不能为空");
		}
		
		return domainConfigService.add(dto);
	}
	
	//更新dns平台配置
	@PutMapping("/update")
	public SaResult update(@RequestBody @NotNull DomainConfig dto) {
		if (StrUtil.hasBlank(dto.getDns())) {
			return SaResult.error("DNS不能为空");
		} else if (StrUtil.hasBlank(dto.getConfig())) {
			return SaResult.error("配置不能为空");
		}
		
		return domainConfigService.update(dto);
	}
	
	//DNS平台配置列表
	@GetMapping("/list")
	public SaResult list(@RequestParam(required = false, defaultValue = "1", value = "page") int page,
	                     @RequestParam(required = false, defaultValue = "10", value = "pageSize") int pageSize) {
		return domainConfigService.list(page, pageSize);
	}
	
	//删除配置
	@DeleteMapping("/delete/{dns}")
	public SaResult delete(@PathVariable("dns") String dns) {
		if (dns == null) {
			return SaResult.error("缺少参数");
		}
		
		return domainConfigService.delete(dns);
	}
}
