package com.guaning.newlangs;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
@MapperScan(basePackages = {"com.guaning.newlangs.mapper"})
public class NewlangsApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(NewlangsApplication.class, args);
	}
	
}
