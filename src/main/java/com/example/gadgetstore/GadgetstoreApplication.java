package com.example.gadgetstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class GadgetstoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(GadgetstoreApplication.class, args);
	}

}
