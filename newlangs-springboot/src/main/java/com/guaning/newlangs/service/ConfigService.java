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
	SaResult update(Collection<Config> entityList);

	List<Config> list();

	SaResult scan() throws IOException;

	int startScan() throws IOException;
}
