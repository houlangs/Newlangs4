package com.guaning.newlangs.service;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guaning.newlangs.dto.DomainRecordCommonDto;
import com.guaning.newlangs.entity.DomainRecord;

public interface DomainRecordService extends IService<DomainRecord> {
	SaResult add(DomainRecordCommonDto dto);

	SaResult detail(Long id);

	SaResult update(DomainRecordCommonDto dto);

	SaResult delete(Long id);

	SaResult list(int page, int pageSize);
}
