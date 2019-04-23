package com.zwk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.zwk.config.datasource", "com.zwk.demo"})
public class DesignPatternTemplateApplication {

	public static void main(String[] args) {
		SpringApplication.run(DesignPatternTemplateApplication.class, args);
	}

}
