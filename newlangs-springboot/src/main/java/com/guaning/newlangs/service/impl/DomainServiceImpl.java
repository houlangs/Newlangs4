package com.guaning.newlangs.service.impl;

import cn.dev33.satoken.util.SaResult;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guaning.newlangs.apis.CloudFlareAPI;
import com.guaning.newlangs.entity.Domain;
import com.guaning.newlangs.entity.DomainRecord;
import com.guaning.newlangs.mapper.DomainMapper;
import com.guaning.newlangs.service.DomainConfigService;
import com.guaning.newlangs.service.DomainRecordService;
import com.guaning.newlangs.service.DomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DomainServiceImpl extends ServiceImpl<DomainMapper, Domain> implements DomainService {
    private final CloudFlareAPI cf;
    private final DomainConfigService domainConfigService;

    public DomainServiceImpl(CloudFlareAPI cf, DomainConfigService domainConfigService) {
        this.cf = cf;
        this.domainConfigService = domainConfigService;
    }

    private DomainRecordService domainRecordService;

    @Autowired
    public void setDomainRecordService(DomainRecordService domainRecordService) {
        this.domainRecordService = domainRecordService;
    }

    // Get Domain Information
    @Override
    public SaResult detail(Long id) {
        // Check if the domain exists
        Domain domainOne = getById(id);
        if (domainOne == null) {
            return SaResult.error("Domain does not exist");
        }

        return SaResult.data(domainOne);
    }

    // Add Domain
    @Override
    public SaResult add(Domain dto) {
        // Check if the domain exists
        Domain domainOne = getOne(Wrappers.<Domain>lambdaQuery().eq(Domain::getName, dto.getName()));
        if (domainOne != null) {
            return SaResult.error("Domain already exists");
        }

        Domain domain = new Domain();
        domain.setDomainId(dto.getDomainId());
        domain.setDns(dto.getDns());
        domain.setName(dto.getName());
        domain.setRoleIds(dto.getRoleIds());
        domain.setPoint(dto.getPoint());
        domain.setComment(dto.getComment());
        domain.setCreatedTime(LocalDateTime.now());
        domain.setUpdatedTime(LocalDateTime.now());
        save(domain);

        return SaResult.ok("Added successfully");
    }

    // Update Domain Information
    @Override
    public SaResult update(Domain dto) {
        // Check if the domain exists
        Domain domainOne = getOne(Wrappers.<Domain>lambdaQuery().eq(Domain::getId, dto.getId()));
        if (domainOne == null) {
            return SaResult.error("Domain does not exist");
        }

        Domain domain = new Domain();
        domain.setId(dto.getId());
        domain.setRoleIds(dto.getRoleIds());
        domain.setPoint(dto.getPoint());
        domain.setComment(dto.getComment());
        domain.setUpdatedTime(LocalDateTime.now());
        updateById(domain);

        return SaResult.ok("Updated successfully");
    }

    // Delete Domain
    @Override
    public SaResult delete(Long id) {
        // Check if the domain exists
        Domain domainOne = getById(id);
        if (domainOne == null) {
            return SaResult.error("Domain does not exist");
        }

        // Query domain records by 'domain_id'
        DomainRecord record = domainRecordService.getOne(Wrappers.<DomainRecord>lambdaQuery().eq(DomainRecord::getDid, domainOne.getId()));
        if (record != null) {
            // Domain record ID
            Long recordId = record.getId();
            domainRecordService.removeById(recordId);
            removeById(id);
        } else {
            removeById(id);
        }

        return SaResult.ok("Deleted successfully");
    }

    // Get Domain List from Database
    @Override
    public SaResult list(int page, int pageSize) {
        // Create a pagination builder
        Page<Domain> domainPage = new Page<>(page, pageSize);

        // Sort by creation time
        page(domainPage, Wrappers.<Domain>lambdaQuery().orderByAsc(Domain::getCreatedTime));

        return SaResult.data(domainPage);
    }

    // Get Domain List from DNS Platform
    @Override
    public SaResult domainListFromDNS(String dns) {
        // Get DNS configuration
        JSONObject data = JSON.parseObject(JSON.toJSONString(domainConfigService.getOne(dns).getData()));
        JSONObject config = JSON.parseObject(data.getString("config"));
        String email = config.getString("email");
        String key = config.getString("key");

        JSONArray domainList = new JSONArray();
        switch (dns) {
            case "CloudFlare" -> {
                // Call CloudFlare HTTP API and process the returned information
                JSONArray result = JSON.parseObject(cf.listZones(email, key, 1, 1000)).getJSONArray("result");
                if (result == null) {
                    return SaResult.error("Network issue, please try again");
                } else {
                    for (int i = 0; i < result.size(); i++) {
                        JSONObject domainMap = new JSONObject();
                        domainMap.put("domainId", JSON.parseObject(result.getString(i)).getString("id"));
                        domainMap.put("name", JSON.parseObject(result.getString(i)).getString("name"));
                        domainList.add(domainMap);
                    }
                }
            }
            case "DnsPod" -> {
            }
                /*
                  To be implemented
                 */
            case "AliYun" -> {
            }
                /*
                  To be implemented
                 */
        }

        return SaResult.data(domainList);
    }
}
