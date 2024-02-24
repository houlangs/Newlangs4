package com.guaning.newlangs.service.impl;

import cn.dev33.satoken.exception.NotRoleException;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guaning.newlangs.apis.CloudFlareAPI;
import com.guaning.newlangs.dto.DomainRecordCommonDto;
import com.guaning.newlangs.entity.*;
import com.guaning.newlangs.mapper.DomainRecordMapper;
import com.guaning.newlangs.service.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class DomainRecordServiceImpl extends ServiceImpl<DomainRecordMapper, DomainRecord> implements DomainRecordService {
	private final CloudFlareAPI cf;
	private final DomainService domainService;
	private final DomainConfigService domainConfigService;
	private final UserService userService;
	private final PointRecordService pointRecordService;
	
	public DomainRecordServiceImpl(CloudFlareAPI cf, DomainService domainService, DomainConfigService domainConfigService, UserService userService, PointRecordService pointRecordService) {
		this.cf = cf;
		this.domainService = domainService;
		this.domainConfigService = domainConfigService;
		this.userService = userService;
		this.pointRecordService = pointRecordService;
	}
	
	//添加域名记录
	@Override
	public SaResult add(DomainRecordCommonDto dto) {
		//根据did查询域名是否存在
		Domain domainOne = domainService.getOne(Wrappers.<Domain>lambdaQuery().eq(Domain::getId, dto.getDid()));
		if (domainOne == null) {
			return SaResult.error("域名不存在");
		}
		
		//域名存在，则赋值
		dto.setDomainId(domainOne.getDomainId());
		
		//根据did查询使用该域名的解析列表
		List<Object> recordList = listObjs(Wrappers.<DomainRecord>lambdaQuery().eq(DomainRecord::getDid, dto.getDid()));
		if (recordList != null && !recordList.isEmpty()) {
			//查询使用该前缀的域名记录是否存在
			DomainRecord prefixOne = getOne(Wrappers.<DomainRecord>lambdaQuery().eq(DomainRecord::getPrefix, dto.getPrefix()));
			if (prefixOne != null) {
				return SaResult.error("域名前缀已存在");
			}
		}
		
		//域名所属DNS
		String dns = domainOne.getDns();
		//域名角色权限
		String[] roleIds = domainOne.getRoleIds().split(",");
		
		boolean flag = false;
		try {
			StpUtil.checkRoleOr(roleIds);
			flag = true;
		} catch (NotRoleException e) {
			String key = e.getLoginType();
            String role = e.getRole();
			System.out.println("key=>"+key+" role=>"+role);
		}
		
		//域名价格
		Integer point = domainOne.getPoint();
		//用户剩余积分
		JSONObject userJson = JSON.parseObject(JSON.toJSONString(userService.getOne().getData()));
		System.out.println(userJson);
		Integer userPoint = userJson.getInteger("point");
		
		//查询domain_config获取DNS配置
		JSONObject data = JSON.parseObject(JSON.toJSONString(domainConfigService.getOne(dns).getData()));
		JSONObject config = JSON.parseObject(data.getString("config"));
		String email = config.getString("email");
		String key = config.getString("key");
		dto.setEmail(email);
		dto.setKey(key);
		
		switch (dns) {
			case "CloudFlare":
				JSONObject response;
				if (flag) {
					if (point <= userPoint) {
						response = JSON.parseObject(cf.addRecord(dto));
						if (response.getString("success").equals("false")) {
							return SaResult.error(response.getJSONArray("errors").getJSONObject(0).getString("message"));
						}
						
						//插入域名记录数据库
						JSONObject result = response.getJSONObject("result");
						DomainRecord domainRecord = new DomainRecord();
						domainRecord.setUserId(StpUtil.getLoginIdAsLong());
						domainRecord.setDid(dto.getDid());
						domainRecord.setRecordId(result.getString("id"));
						domainRecord.setPrefix(dto.getPrefix());
						domainRecord.setType(result.getString("type"));
						domainRecord.setValue(result.getString("content"));
						domainRecord.setLineId(result.getString("proxied").equals("true") ? 1 : 0);
						domainRecord.setLine(result.getString("proxied").equals("true") ? "CloudFlare线路" : "其他线路");
						domainRecord.setComment(result.getString("comment"));
						domainRecord.setTtl(result.getInteger("ttl"));
						domainRecord.setCreatedTime(LocalDateTime.now());
						save(domainRecord);
						
						//插入积分记录数据库
						PointRecord pointRecord = new PointRecord();
						pointRecord.setUserId(StpUtil.getLoginIdAsLong());
						pointRecord.setAction("消费");
						pointRecord.setBalance(-point);
						pointRecord.setRest(userPoint - point);
						pointRecord.setRemark("添加记录[" + dto.getPrefix() + "." + domainOne.getName() + "](" + domainRecord.getLine() + ")");
						pointRecord.setCreatedTime(LocalDateTime.now());
						pointRecordService.add(pointRecord);
						
						//更新用户积分
						User user = new User();
						user.setId(dto.getUserId());
						user.setPoint(userPoint - point);
						userService.updateById(user);
					} else {
						return SaResult.error("积分不足，请充值");
					}
				} else {
					return SaResult.error("添加失败，权限不足");
				}
			case "DnsPod":
				/**
				 * 待做
				 */
				break;
		}
		return SaResult.ok("添加成功");
	}
	
	//域名记录详情
	@Override
	public SaResult detail(Long id) {
		//查询域名记录是否存在
		DomainRecord recordOne = getById(id);
		if (recordOne == null) {
			return SaResult.error("域名记录不存在");
		}
		
		return SaResult.data(recordOne);
	}
	
	//更新域名记录
	@Override
	public SaResult update(DomainRecordCommonDto dto) {
		//根据did查询域名是否存在
		Domain domainOne = domainService.getOne(Wrappers.<Domain>lambdaQuery().eq(Domain::getId, dto.getDid()));
		if (domainOne == null) {
			return SaResult.error("域名不存在");
		}
		
		//域名存在，则赋值
		dto.setDomainId(domainOne.getDomainId());
		
		//根据did查询使用该域名的解析列表
		//List<Object> recordList = listObjs(Wrappers.<DomainRecord>lambdaQuery().eq(DomainRecord::getDid, dto.getDid()));
		//if (recordList != null && !recordList.isEmpty()) {
			//查询使用该前缀的域名记录是否存在
			DomainRecord prefixOne = getOne(Wrappers.<DomainRecord>lambdaQuery().eq(DomainRecord::getPrefix, dto.getPrefix()).eq(DomainRecord::getDid, dto.getDid()));
			if (!prefixOne.getId().equals(dto.getId())) {
				return SaResult.error("域名前缀已存在");
			}
		//}
		
		dto.setRecordId(prefixOne.getRecordId());
		//域名所属DNS
		String dns = domainOne.getDns();
		//域名角色权限
		String[] roleIds = domainOne.getRoleIds().split(",");
		
		boolean flag = false;
		try {
			StpUtil.checkRoleOr(roleIds);
			flag = true;
		} catch (NotRoleException e) {
			String key = e.getLoginType();
            String role = e.getRole();
			System.out.println("key=>"+key+" role=>"+role);
		}
		
		//查询domain_config获取DNS配置
		DomainConfig domainConfig = domainConfigService.getOne(Wrappers.<DomainConfig>lambdaQuery().eq(DomainConfig::getDns, dns));
		JSONObject config = JSON.parseObject(domainConfig.getConfig());
		String email = config.getString("email");
		String key = config.getString("key");
		dto.setEmail(email);
		dto.setKey(key);
		
		switch (dns) {
			case "CloudFlare":
				JSONObject response;
				if (flag) {
					response = JSON.parseObject(cf.updateRecord(dto));
					System.out.println(response);
					if (response.getString("error") != null) {
						return SaResult.error("更新解析记录失败");
					}
					
					JSONObject result = response.getJSONObject("result");
					DomainRecord domainRecord = new DomainRecord();
					domainRecord.setId(dto.getId());
					domainRecord.setUserId(StpUtil.getLoginIdAsLong());
					domainRecord.setDid(dto.getDid());
					domainRecord.setRecordId(result.getString("id"));
					domainRecord.setPrefix(dto.getPrefix());
					domainRecord.setType(result.getString("type"));
					domainRecord.setValue(result.getString("content"));
					domainRecord.setLineId(result.getString("proxied").equals("true") ? 1 : 0);
					domainRecord.setLine(result.getString("proxied").equals("true") ? "CloudFlare线路" : "其他线路");
					domainRecord.setComment(result.getString("comment"));
					domainRecord.setTtl(result.getInteger("ttl"));
					domainRecord.setUpdatedTime(LocalDateTime.now());
					updateById(domainRecord);
				} else {
					return SaResult.error("更新失败，权限不足");
				}
			case "DnsPod":
				/**
				 * 待做
				 */
				break;
		}
		return SaResult.ok("更新成功");
	}
	
	//删除域名记录
	@Override
	public SaResult delete(Long id) {
		//数据库查询域名记录是否存在
		DomainRecord domainRecord = getById(id);
		if (domainRecord == null) {
			return SaResult.error("域名记录不存在");
		}
		
		//通过domain_id查询domain详细信息
		Domain domain = domainService.getOne(Wrappers.<Domain>lambdaQuery().eq(Domain::getId, domainRecord.getDid()));
		//获取DNS和role
		String dns = domain.getDns();
		//查询domain_config获取DNS配置
		DomainConfig domainConfig = domainConfigService.getOne(Wrappers.<DomainConfig>lambdaQuery().eq(DomainConfig::getDns, dns));
		JSONObject config = JSON.parseObject(domainConfig.getConfig());
		String email = config.getString("email");
		String key = config.getString("key");
		DomainRecordCommonDto dto = new DomainRecordCommonDto();
		dto.setDomainId(domain.getDomainId());
		dto.setRecordId(domainRecord.getRecordId());
		dto.setEmail(email);
		dto.setKey(key);
		
		switch (dns) {
			case "CloudFlare":
				JSONObject response;
				response = JSON.parseObject(cf.deleteRecord(dto));
				System.out.println(response);
				if (response.getString("success").equals("false")) {
					return SaResult.error(response.getJSONArray("errors").getJSONObject(0).getString("message"));
				}
				
				removeById(id);
			case "DnsPod":
				/**
				 * 待做
				 */
				break;
		}
		
		return SaResult.ok("删除成功");
	}
	
	//获取域名记录列表
	@Override
	public SaResult list(int page, int pageSize) {
		//构造分页构造器
		Page<DomainRecord> recordPage = new Page<>(page, pageSize);
		
		if (StpUtil.hasRole("1")) {
			page(recordPage, Wrappers.<DomainRecord>lambdaQuery().orderByAsc(DomainRecord::getCreatedTime));
		} else {
			page(recordPage, Wrappers.<DomainRecord>lambdaQuery().eq(DomainRecord::getUserId, StpUtil.getLoginIdAsLong()).orderByDesc(DomainRecord::getCreatedTime));
		}
		
		return SaResult.data(recordPage);
	}
}
