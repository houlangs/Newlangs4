package com.guaning.newlangs.config;

import com.guaning.newlangs.common.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Slf4j
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	@Autowired
	private RequestLimitIntercept requestLimitIntercept;
	
	/**
	 * 扩展mvc框架的消息转换器
	 */
	@Override
	public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
		//创建消息转换器对象
		MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
		//设置对象转换器，底层Jackson
		messageConverter.setObjectMapper(new JacksonObjectMapper());
		//将上面的消息转换器对象追加到mvc框架的转换器集合
		converters.add(0, messageConverter);
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		log.info("添加拦截");
		registry.addInterceptor(requestLimitIntercept);
	}
}
