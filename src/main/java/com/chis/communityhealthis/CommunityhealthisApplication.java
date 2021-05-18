package com.chis.communityhealthis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class CommunityhealthisApplication {

	public static void main(String[] args) {
		SpringApplication.run(CommunityhealthisApplication.class, args);
	}

}
