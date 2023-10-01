package com.guaning.newlangs;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.extra.tokenizer.Result;
import cn.hutool.extra.tokenizer.TokenizerEngine;
import cn.hutool.extra.tokenizer.TokenizerUtil;
import cn.hutool.extra.tokenizer.Word;
import cn.hutool.extra.tokenizer.engine.hanlp.HanLPEngine;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootTest
class NewlangsSpringbootApplicationTests {
	@Test
	public void scan() throws IOException {
		Document document = Jsoup.connect("http://new.langs.ink").timeout(10000).get();
		String htmlString = document.html();
		List<String> chineseCharacterList = new ArrayList<>();
		String documentString;
		
		//使用正则表达式匹配汉字
        Pattern pattern = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher matcher = pattern.matcher(htmlString);
		
		//迭代匹配结果
        while (matcher.find()) {
            String chineseCharacter = matcher.group();
 
            //保存每个汉字
            chineseCharacterList.add(chineseCharacter);
        }
		documentString = String.join("", chineseCharacterList);
		System.out.println(documentString);
		String keyWord = "厚浪";
		if (documentString.contains(keyWord)) {
			System.out.println("检查出违规词！");
		} else {
			System.out.println("未发现违规词");
		}
	}
}
