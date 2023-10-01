package com.guaning.newlangs.service;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guaning.newlangs.entity.Domain;

public interface DomainService extends IService<Domain> {
	SaResult detail(Long id);

	SaResult add(Domain dto);

	SaResult update(Domain dto);

	SaResult delete(Long id);

	SaResult list(int page, int pageSize);

	SaResult domainListFromDNS(String dns);
}
