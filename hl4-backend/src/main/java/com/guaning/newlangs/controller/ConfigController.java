package com.guaning.newlangs.controller;

import cn.dev33.satoken.util.SaResult;
import com.guaning.newlangs.entity.Config;
import com.guaning.newlangs.service.ConfigService;
import com.guaning.newlangs.service.UserService;
import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/config")
public class ConfigController {
	private ConfigService configService;
	
	@Autowired
	public void setConfigService(ConfigService configService) {
		this.configService = configService;
	}
	
	//更新配置
	@PutMapping("/update")
	public SaResult update(@RequestBody @NotNull Collection<Config> entityList) {
		return configService.update(entityList);
	}

	//配置列表
	@GetMapping("/list")
	public List<Config> list() {
		return configService.list();
	}
	
	//扫描
	@GetMapping("/scan")
	public SaResult scan() throws IOException {
		return configService.scan();
	}
}
