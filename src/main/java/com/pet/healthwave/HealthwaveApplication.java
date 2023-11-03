package com.pet.healthwave;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class HealthwaveApplication {
	// todo: logs
	// todo: delete user after 15 min
	public static void main(String[] args) {
		SpringApplication.run(HealthwaveApplication.class, args);
	}

}
