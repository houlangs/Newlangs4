package com.guaning.newlangs.service.impl;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guaning.newlangs.entity.DomainConfig;
import com.guaning.newlangs.mapper.DomainConfigMapper;
import com.guaning.newlangs.service.DomainConfigService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DomainConfigServiceImpl extends ServiceImpl<DomainConfigMapper, DomainConfig> implements DomainConfigService {
    // Add DNS Platform Configuration
    @Override
    public SaResult add(DomainConfig dto) {
        // Check if it already exists
        DomainConfig one = getById(dto.getDns());
        if (one != null) {
            return SaResult.error("This DNS already exists, please choose another platform");
        }

        dto.setCreatedTime(LocalDateTime.now());
        save(dto);

        return SaResult.ok("Added successfully");
    }

    // Update DNS Platform Configuration
    @Override
    public SaResult update(DomainConfig dto) {
        // Check if it exists
        DomainConfig one = getById(dto.getDns());
        if (one == null) {
            return SaResult.error("This DNS does not exist");
        }

        dto.setUpdatedTime(LocalDateTime.now());
        updateById(dto);

        return SaResult.ok("Updated successfully");
    }

    // Get DNS Platform Configuration
    @Override
    public SaResult getOne(String dns) {
        // Check if it exists
        DomainConfig one = getById(dns);
        if (one == null) {
            return SaResult.error("This DNS does not exist");
        }

        return SaResult.data(one);
    }

    // DNS Platform Configuration List
    @Override
    public SaResult list(int page, int pageSize) {
        // Create a pagination builder
        Page<DomainConfig> domainConfigPage = new Page<>(page, pageSize);

        // Order by creation time
        page(domainConfigPage, Wrappers.<DomainConfig>lambdaQuery().orderByAsc(DomainConfig::getCreatedTime));

        return SaResult.data(domainConfigPage);
    }

    // Delete Configuration
    @Override
    public SaResult delete(String dns) {
        // Check if it exists
        DomainConfig one = getById(dns);
        if (one == null) {
            SaResult.error("This DNS does not exist");
        }

        removeById(dns);

        return SaResult.ok("Deleted successfully");
    }
}
