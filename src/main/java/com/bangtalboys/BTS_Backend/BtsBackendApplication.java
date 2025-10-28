package com.bangtalboys.BTS_Backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.PropertySource;

@EnableFeignClients
@SpringBootApplication
@PropertySource("file:.env")
public class BtsBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BtsBackendApplication.class, args);
	}
}
