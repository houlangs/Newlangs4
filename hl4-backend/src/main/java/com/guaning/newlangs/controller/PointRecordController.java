package com.guaning.newlangs.controller;

import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.util.StrUtil;
import com.guaning.newlangs.dto.BuyPointDto;
import com.guaning.newlangs.entity.PointRecord;
import com.guaning.newlangs.service.DomainConfigService;
import com.guaning.newlangs.service.PointRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/point_record")
public class PointRecordController {
	private PointRecordService pointRecordService;
	
	@Autowired
	public void setPointRecordService(PointRecordService pointRecordService) {
		this.pointRecordService = pointRecordService;
	}
	
	//积分记录列表
	@GetMapping("/list")
	public SaResult list(@RequestParam(required = false, defaultValue = "1", value = "page") int page,
	                     @RequestParam(required = false, defaultValue = "10", value = "pageSize") int pageSize) {
		return pointRecordService.list(page, pageSize);
	}
	
	//修改积分
	@PostMapping("/edit")
	public SaResult edit(@RequestBody PointRecord dto) {
		if (dto.getUserId() == null) {
			return SaResult.error("未获取到有效参数");
		} else if (StrUtil.hasBlank(dto.getAction())) {
			return SaResult.error("积分操作不能为空");
		} else if (dto.getBalance() == null) {
			return SaResult.error("积分数目不能为空");
		} else if (StrUtil.hasBlank(dto.getRemark())) {
			return SaResult.error("积分备注不能为空");
		}
		
		return pointRecordService.edit(dto);
	}
	
	//生成积分兑换码
	@GetMapping("/createCode")
	public SaResult createPointCode(@RequestParam int point, @RequestParam String apiKey) {
		if (!apiKey.equals("9cd5f566b9b69245")) {
			return SaResult.error("校验失败");
		} else if (point <= 0) {
			return SaResult.error("参数错误");
		} else {
			return pointRecordService.createPointCode(point);
		}
	}
	
	//兑换积分
	@GetMapping("/exchangePoint")
	public SaResult exchangePoint(@RequestParam String pointCode) {
		if (pointCode == null) {
			return SaResult.error("兑换码错误");
		} else {
			return pointRecordService.exchangePoint(pointCode);
		}
	}
}
