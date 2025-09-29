package com.nsteuerberg.backend.virma;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.nsteuerberg.backend.virma.service.http")
public class VirmaApplication {

	public static void main(String[] args) {
		SpringApplication.run(VirmaApplication.class, args);
	}

}
