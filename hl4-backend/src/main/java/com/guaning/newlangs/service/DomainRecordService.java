package com.guaning.newlangs.service;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guaning.newlangs.dto.DomainRecordCommonDto;
import com.guaning.newlangs.entity.DomainRecord;

public interface DomainRecordService extends IService<DomainRecord> {
	//添加域名记录
	SaResult add(DomainRecordCommonDto dto);
	
	//域名记录详情
	SaResult detail(Long id);
	
	//更新域名记录
	SaResult update(DomainRecordCommonDto dto);
	
	//删除域名记录
	SaResult delete(Long id);
	
	//获取域名记录列表
	SaResult list(int page, int pageSize);
}
