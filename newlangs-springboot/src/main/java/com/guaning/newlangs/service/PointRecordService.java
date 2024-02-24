package com.guaning.newlangs.service;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guaning.newlangs.dto.BuyPointDto;
import com.guaning.newlangs.entity.PointRecord;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface PointRecordService extends IService<PointRecord> {
	//积分记录列表
	SaResult list(int page, int pageSize);
	
	//添加积分记录
	SaResult add(PointRecord dto);
	
	//修改积分
	SaResult edit(PointRecord dto);
	
	//生成积分兑换码
	SaResult createPointCode(int point);
	
	//兑换积分
	SaResult exchangePoint(String pointCode);
}
