package com.guaning.newlangs.service;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guaning.newlangs.entity.Config;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.List;

public interface ConfigService extends IService<Config> {
	//修改参数值
	SaResult update(Collection<Config> entityList);
	
	//获取所有参数的键值对
	List<Config> list();
	
	//扫描违规
	SaResult scan() throws IOException;
	
	//扫描具体逻辑
	int startScan() throws IOException;
}
