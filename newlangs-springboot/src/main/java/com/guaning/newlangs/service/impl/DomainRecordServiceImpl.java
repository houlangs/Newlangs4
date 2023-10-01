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

    // Add Domain Record
    @Override
    public SaResult add(DomainRecordCommonDto dto) {
        // Check if the domain with 'did' exists
        Domain domainOne = domainService.getOne(Wrappers.<Domain>lambdaQuery().eq(Domain::getId, dto.getDid()));
        if (domainOne == null) {
            return SaResult.error("Domain does not exist");
        }

        // Assign 'domainId' if the domain exists
        dto.setDomainId(domainOne.getDomainId());

        // Check if there are existing records for the same 'did'
        List<Object> recordList = listObjs(Wrappers.<DomainRecord>lambdaQuery().eq(DomainRecord::getDid, dto.getDid()));
        if (recordList != null && !recordList.isEmpty()) {
            // Check if a record with the same prefix already exists
            DomainRecord prefixOne = getOne(Wrappers.<DomainRecord>lambdaQuery().eq(DomainRecord::getPrefix, dto.getPrefix()));
            if (prefixOne != null) {
                return SaResult.error("Domain prefix already exists");
            }
        }

        // Get the DNS of the domain
        String dns = domainOne.getDns();
        // Get the role permissions of the domain
        String[] roleIds = domainOne.getRoleIds().split(",");

        boolean flag = false;
        try {
            StpUtil.checkRoleOr(roleIds);
            flag = true;
        } catch (NotRoleException e) {
            String key = e.getLoginType();
            String role = e.getRole();
            System.out.println("key=>" + key + " role=>" + role);
        }

        // Get the price of the domain
        Integer point = domainOne.getPoint();
        // Get the remaining points of the user
        JSONObject userJson = JSON.parseObject(JSON.toJSONString(userService.getOne().getData()));
        System.out.println(userJson);
        Integer userPoint = userJson.getInteger("point");

        // Get DNS configuration from domain_config
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
                            return SaResult.error(Objects.requireNonNull(response.getJSONArray("errors").getJSONObject(0)).getString("message"));
                        }

                        // Insert domain record into the database
                        JSONObject result = response.getJSONObject("result");
                        DomainRecord domainRecord = new DomainRecord();
                        domainRecord.setUserId(StpUtil.getLoginIdAsLong());
                        domainRecord.setDid(dto.getDid());
                        domainRecord.setRecordId(result.getString("id"));
                        domainRecord.setPrefix(dto.getPrefix());
                        domainRecord.setType(result.getString("type"));
                        domainRecord.setValue(result.getString("content"));
                        domainRecord.setLineId(result.getString("proxied").equals("true") ? 1 : 0);
                        domainRecord.setLine(result.getString("proxied").equals("true") ? "CloudFlare Line" : "Other Line");
                        domainRecord.setComment(result.getString("comment"));
                        domainRecord.setTtl(result.getInteger("ttl"));
                        domainRecord.setCreatedTime(LocalDateTime.now());
                        save(domainRecord);

                        // Insert point record into the database
                        PointRecord pointRecord = new PointRecord();
                        pointRecord.setUserId(StpUtil.getLoginIdAsLong());
                        pointRecord.setAction("Expense");
                        pointRecord.setBalance(-point);
                        pointRecord.setRest(userPoint - point);
                        pointRecord.setRemark("Add Record [" + dto.getPrefix() + "." + domainOne.getName() + "](" + domainRecord.getLine() + ")");
                        pointRecord.setCreatedTime(LocalDateTime.now());
                        pointRecordService.add(pointRecord);

                        // Update user points
                        User user = new User();
                        user.setId(dto.getUserId());
                        user.setPoint(userPoint - point);
                        userService.updateById(user);
                    } else {
                        return SaResult.error("Insufficient points, please recharge");
                    }
                } else {
                    return SaResult.error("Add failed, insufficient permissions");
                }
            case "DnsPod":
                /**
                 * To be implemented
                 */
                break;
        }
        return SaResult.ok("Added successfully");
    }

    // Domain Record Details
    @Override
    public SaResult detail(Long id) {
        // Check if the domain record exists in the database
        DomainRecord recordOne = getById(id);
        if (recordOne == null) {
            return SaResult.error("Domain record does not exist");
        }

        return SaResult.data(recordOne);
    }

    // Update Domain Record
    @Override
    public SaResult update(DomainRecordCommonDto dto) {
        // Check if the domain with 'did' exists
        Domain domainOne = domainService.getOne(Wrappers.<Domain>lambdaQuery().eq(Domain::getId, dto.getDid()));
        if (domainOne == null) {
            return SaResult.error("Domain does not exist");
        }

        // Assign 'domainId' if the domain exists
        dto.setDomainId(domainOne.getDomainId());

        // Check if there are existing records for the same 'did'
        // List<Object> recordList = listObjs(Wrappers.<DomainRecord>lambdaQuery().eq(DomainRecord::getDid, dto.getDid()));
        // if (recordList != null && !recordList.isEmpty()) {
        // Check if a record with the same prefix already exists
        DomainRecord prefixOne = getOne(Wrappers.<DomainRecord>lambdaQuery().eq(DomainRecord::getPrefix, dto.getPrefix()).eq(DomainRecord::getDid, dto.getDid()));
        if (prefixOne != null && !prefixOne.getId().equals(dto.getId())) {
            return SaResult.error("Domain prefix already exists");
        }
        // }

        dto.setRecordId(prefixOne.getRecordId());
        // Get the DNS of the domain
        String dns = domainOne.getDns();
        // Get the role permissions of the domain
        String[] roleIds = domainOne.getRoleIds().split(",");

        boolean flag = false;
        try {
            StpUtil.checkRoleOr(roleIds);
            flag = true;
        } catch (NotRoleException e) {
            String key = e.getLoginType();
            String role = e.getRole();
            System.out.println("key=>" + key + " role=>" + role);
        }

        // Get DNS configuration from domain_config
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
                        return SaResult.error("Failed to update DNS record");
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
                    domainRecord.setLine(result.getString("proxied").equals("true") ? "CloudFlare Line" : "Other Line");
                    domainRecord.setComment(result.getString("comment"));
                    domainRecord.setTtl(result.getInteger("ttl"));
                    domainRecord.setUpdatedTime(LocalDateTime.now());
                    updateById(domainRecord);
                } else {
                    return SaResult.error("Update failed, insufficient permissions");
                }
            case "DnsPod":
                /**
                 * To be implemented
                 */
                break;
        }
        return SaResult.ok("Updated successfully");
    }

    // Delete Domain Record
    @Override
    public SaResult delete(Long id) {
        // Check if the domain record exists in the database
        DomainRecord domainRecord = getById(id);
        if (domainRecord == null) {
            return SaResult.error("Domain record does not exist");
        }

        // Find detailed information about the domain through 'domain_id'
        Domain domain = domainService.getOne(Wrappers.<Domain>lambdaQuery().eq(Domain::getId, domainRecord.getDid()));
        // Get DNS and role permissions
        String dns = domain.getDns();
        // Get DNS configuration from domain_config
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
                    return SaResult.error(Objects.requireNonNull(response.getJSONArray("errors").getJSONObject(0)).getString("message"));
                }

                removeById(id);
            case "DnsPod":
                /**
                 * To be implemented
                 */
                break;
        }

        return SaResult.ok("Deleted successfully");
    }

    // Get Domain Record List
    @Override
    public SaResult list(int page, int pageSize) {
        // Create a pagination builder
        Page<DomainRecord> recordPage = new Page<>(page, pageSize);

        if (StpUtil.hasRole("1")) {
            page(recordPage, Wrappers.<DomainRecord>lambdaQuery().orderByAsc(DomainRecord::getCreatedTime));
        } else {
            page(recordPage, Wrappers.<DomainRecord>lambdaQuery().eq(DomainRecord::getUserId, StpUtil.getLoginIdAsLong()).orderByAsc(DomainRecord::getCreatedTime));
        }

        return SaResult.data(recordPage);
    }
}
