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
	@Override
	public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
		MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
		messageConverter.setObjectMapper(new JacksonObjectMapper());
		converters.add(0, messageConverter);
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		log.info("添加拦截");
		registry.addInterceptor(requestLimitIntercept);
	}
}
