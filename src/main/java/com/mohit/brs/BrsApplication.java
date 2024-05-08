package com.mohit.brs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
public class BrsApplication {

	public static void main(String[] args) {
		SpringApplication.run(BrsApplication.class, args);
	}

}
