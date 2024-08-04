package com.guaning.newlangs.service.impl;

import cn.dev33.satoken.util.SaResult;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guaning.newlangs.apis.CloudFlareAPI;
import com.guaning.newlangs.dto.DomainRecordCommonDto;
import com.guaning.newlangs.entity.Config;
import com.guaning.newlangs.entity.Domain;
import com.guaning.newlangs.entity.DomainRecord;
import com.guaning.newlangs.mapper.ConfigMapper;
import com.guaning.newlangs.service.ConfigService;
import com.guaning.newlangs.service.DomainConfigService;
import com.guaning.newlangs.service.DomainRecordService;
import com.guaning.newlangs.service.DomainService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ConfigServiceImpl extends ServiceImpl<ConfigMapper, Config> implements ConfigService {
	private final CloudFlareAPI cf;
	private final DomainService domainService;
	private final DomainRecordService domainRecordService;

	public ConfigServiceImpl(CloudFlareAPI cf, DomainService domainService, DomainRecordService domainRecordService) {
		this.cf = cf;
		this.domainService = domainService;
		this.domainRecordService = domainRecordService;

	}

	// 更新配置
	@Override
	public SaResult update(Collection<Config> entityList) {
		updateBatchById(entityList);

		return SaResult.ok("更新成功");
	}

	// 所有配置列表
	@Override
	public List<Config> list() {
		return super.list();
	}

	@Override
	public SaResult scan() throws IOException {
		int num = startScan();
		return SaResult.ok("扫描完毕！扫出违规域名" + num + "个.");
	}

	@Scheduled(cron = "0 0/30 * * * ?")
	@Override
	public int startScan() {
		// 获取扫描配置
		List<Config> configList = list();
		List<String> keyWords = new ArrayList<>();
		for (Config con : configList) {
			if (con.getK().equals("scan_keyWords")) {
				keyWords = Arrays.asList(con.getV().split(","));
			}
		}

		// 所有需要扫描的域名列表
		DomainRecordCommonDto dto = new DomainRecordCommonDto();
		List<String> urlList = new ArrayList<>();
		
		List<DomainRecord> recordList = domainRecordService.list(new QueryWrapper<DomainRecord>().lambda().orderByDesc(DomainRecord::getCreatedTime));
		List<Domain> domainList = domainService.list();
		for (DomainRecord prefix : recordList) {
			for (Domain domain : domainList) {
				if (prefix.getDid().equals(domain.getId())) {
					urlList.add("http://" + prefix.getPrefix() + "." + domain.getName());
					dto.setDomainId(domain.getDomainId());
					dto.setRecordId(prefix.getRecordId());
				}
			}
		}

		// 违规url数量
		int num = 0;
		// Jsoup解析网站HTML模板
		for (String url : urlList) {
			Document document;
			try {
				document = Jsoup.connect(url).timeout(30000).get();
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
			String htmlString = document.html();
			List<String> chineseCharacterList = new ArrayList<>();
			String documentString;

			// 使用正则表达式匹配汉字
			Pattern pattern = Pattern.compile("[\u4e00-\u9fa5]");
			Matcher matcher = pattern.matcher(htmlString);

			// 迭代匹配结果
			while (matcher.find()) {
				String chineseCharacter = matcher.group();

				// 保存每个汉字
				chineseCharacterList.add(chineseCharacter);
			}
			documentString = String.join("", chineseCharacterList);

			// 循环匹配关键词
			for (String keyWord : keyWords) {
				if (documentString.contains(keyWord)) {
					// 删除解析记录
					cf.deleteRecord(dto);
					num++;
					// 数据库删除记录
					domainRecordService.remove(
							Wrappers.<DomainRecord>lambdaQuery().eq(DomainRecord::getRecordId, dto.getRecordId()));

					break;
				}
			}
		}

		return num;
	}
}
