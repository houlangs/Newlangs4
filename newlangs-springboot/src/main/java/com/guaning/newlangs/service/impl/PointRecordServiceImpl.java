package com.guaning.newlangs.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guaning.newlangs.dto.BuyPointDto;
import com.guaning.newlangs.entity.Config;
import com.guaning.newlangs.entity.PointRecord;
import com.guaning.newlangs.entity.User;
import com.guaning.newlangs.mapper.PointRecordMapper;
import com.guaning.newlangs.service.ConfigService;
import com.guaning.newlangs.service.PointRecordService;
import com.guaning.newlangs.service.UserService;
import com.guaning.newlangs.util.GenSerial;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class PointRecordServiceImpl extends ServiceImpl<PointRecordMapper, PointRecord> implements PointRecordService {
    final ConfigurableListableBeanFactory beanFactory;
    private final UserService userService;

    @Resource
    private RedisTemplate<String, Integer> redisTemplate;

    public PointRecordServiceImpl(ConfigurableListableBeanFactory beanFactory, UserService userService) {
        this.beanFactory = beanFactory;
        this.userService = userService;
    }

    public ConfigService getConfigService() {
        return beanFactory.getBean(ConfigService.class);
    }

    // Point Record List
    @Override
    public SaResult list(int page, int pageSize) {
        // Create a pagination builder
        Page<PointRecord> recordPage = new Page<>(page, pageSize);

        if (StpUtil.hasRole("1")) {
            page(recordPage, Wrappers.<PointRecord>lambdaQuery().orderByDesc(PointRecord::getCreatedTime));
        } else {
            page(recordPage, Wrappers.<PointRecord>lambdaQuery().eq(PointRecord::getUserId, StpUtil.getLoginIdAsLong())
                    .orderByDesc(PointRecord::getCreatedTime));
        }

        return SaResult.data(recordPage);
    }

    // Add Point Record
    @Override
    public SaResult add(PointRecord dto) {
        save(dto);

        return SaResult.ok("Point record added successfully");
    }

    // Modify Points
    @Override
    public SaResult edit(PointRecord dto) {
        Long userId = dto.getUserId();
        String action = dto.getAction();
        Integer balance = dto.getBalance();
        int rest;
        String remark = dto.getRemark();

        Integer oldPoint = userService.getById(userId).getPoint();
        if (action.equals("Increase")) {
            rest = oldPoint + balance;
        } else if (action.equals("Deduct")) {
            rest = oldPoint - balance;
            if (rest < 0) {
                return SaResult.error("Exceeded the point deduction limit, please re-enter");
            }
        } else {
            return SaResult.error("Invalid Action parameter");
        }

        PointRecord pointRecord = new PointRecord();
        pointRecord.setUserId(userId);
        pointRecord.setAction(action);
        pointRecord.setBalance(balance);
        pointRecord.setRest(rest);
        pointRecord.setRemark(remark);
        pointRecord.setCreatedTime(LocalDateTime.now());
        add(pointRecord);

        User user = new User();
        user.setId(userId);
        user.setPoint(rest);
        user.setUpdatedTime(LocalDateTime.now());
        userService.updateById(user);

        return SaResult.ok("Points modified successfully");
    }

    // Buy Points
    public void buyPoint(BuyPointDto dto) {
    }


}
