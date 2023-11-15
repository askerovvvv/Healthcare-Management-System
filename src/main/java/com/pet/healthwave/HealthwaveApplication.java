package com.pet.healthwave;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class HealthwaveApplication {
	// todo: delete user after 15 min
	public static void main(String[] args) {
		SpringApplication.run(HealthwaveApplication.class, args);
	}

}
