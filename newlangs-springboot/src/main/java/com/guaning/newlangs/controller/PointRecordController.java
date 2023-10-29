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
}
