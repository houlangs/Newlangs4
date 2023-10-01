package com.guaning.newlangs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.annotation.Transactional;

@SpringBootApplication
@ServletComponentScan
public class NewlangsApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(NewlangsApplication.class, args);
	}
	
}
