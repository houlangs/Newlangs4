package com.guaning.newlangs.controller;

import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.util.StrUtil;
import com.guaning.newlangs.dto.DomainRecordCommonDto;
import com.guaning.newlangs.entity.Config;
import com.guaning.newlangs.service.ConfigService;
import com.guaning.newlangs.service.DomainRecordService;
import com.sun.istack.NotNull;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/domain_record")
public class DomainRecordController {
	private final DomainRecordService domainRecordService;
	private final ConfigService configService;
	
	public DomainRecordController(DomainRecordService domainRecordService, ConfigService configService) {
		this.domainRecordService = domainRecordService;
		this.configService = configService;
	}
	
	//添加域名记录
	@PostMapping("/add")
	public SaResult add(@RequestBody @NotNull DomainRecordCommonDto dto) {
		//获取禁止域名前缀配置
		List<Config> configList = configService.list();
		List<String> noPrefixList = new ArrayList<>();
		for (Config con : configList) {
			if (con.getK().equals("no_prefix")) {
				noPrefixList = Arrays.asList(con.getV().split(","));
			}
		}
		
		List<String> list = new ArrayList<>();
		list.add("A");
		list.add("CNAME");
		list.add("TXT");
		
		if (dto.getDid() == null || dto.getUserId() == null) {
			return SaResult.error("未获取到有效参数");
		} else if (StrUtil.hasBlank(dto.getPrefix())) {
			return SaResult.error("域名记录名不能为空");
		} else if (noPrefixList.contains(dto.getPrefix())) {
			return SaResult.error("该前缀禁止解析");
		} else if (StrUtil.hasBlank(dto.getType())) {
			return SaResult.error("记录类型不能为空");
		} else if (!list.contains(dto.getType())) {
			return SaResult.error("记录类型错误");
		} else if (StrUtil.hasBlank(dto.getValue())) {
			return SaResult.error("域名记录值不能为空");
		}
		
		return domainRecordService.add(dto);
	}
	
	//域名记录详情
	@GetMapping("/detail/{id}")
	public SaResult detail(@PathVariable(value = "id") Long id) {
		if (id == null) {
			return SaResult.error("未获取到有效参数");
		}
		
		return domainRecordService.detail(id);
	}
	
	//更新域名记录
	@PutMapping("/update")
	public SaResult update(@RequestBody @NotNull DomainRecordCommonDto dto) {
		//获取禁止域名前缀配置
		List<Config> configList = configService.list();
		List<String> noPrefixList = new ArrayList<>();
		for (Config con : configList) {
			if (con.getK().equals("no_prefix")) {
				noPrefixList = Arrays.asList(con.getV().split(","));
			}
		}
		
		List<String> list = new ArrayList<>();
		list.add("A");
		list.add("CNAME");
		list.add("TXT");
		
		if (dto.getDid() == null) {
			return SaResult.error("未获取到有效参数");
		} else if (StrUtil.hasBlank(dto.getPrefix())) {
			return SaResult.error("域名记录名不能为空");
		} else if (noPrefixList.contains(dto.getPrefix())) {
			return SaResult.error("该前缀禁止解析");
		} else if (StrUtil.hasBlank(dto.getType())) {
			return SaResult.error("记录类型不能为空");
		} else if (!list.contains(dto.getType())) {
			return SaResult.error("记录类型错误");
		} else if (StrUtil.hasBlank(dto.getValue())) {
			return SaResult.error("域名记录值不能为空");
		}
		
		return domainRecordService.update(dto);
	}
	
	//删除域名记录
	@DeleteMapping("/delete/{id}")
	public SaResult delete(@PathVariable(value = "id") Long id) {
		if (id == null) {
			return SaResult.error("参数错误");
		}
		
		return domainRecordService.delete(id);
	}
	
	//域名记录列表
	@GetMapping("/list")
	public SaResult list(@RequestParam(required = false, defaultValue = "1", value = "page") int page,
	                     @RequestParam(required = false, defaultValue = "10", value = "pageSize") int pageSize) {
		return domainRecordService.list(page, pageSize);
	}
}
