package com.guaning.newlangs.service;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guaning.newlangs.dto.BuyPointDto;
import com.guaning.newlangs.entity.PointRecord;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface PointRecordService extends IService<PointRecord> {
	// List of Point Records
	SaResult list(int page, int pageSize);

	// Add Point Record
	SaResult add(PointRecord dto);

	// Edit Point Record
	SaResult edit(PointRecord dto);

	// Buy Points
	void buyPoint(BuyPointDto dto);

	// Asynchronous Notification Interface
	String notify(HttpServletRequest request);

	// Generate Point Redemption Code
	SaResult createPointCode(int point);

	// Redeem Points
	SaResult exchangePoint(String pointCode);
}
