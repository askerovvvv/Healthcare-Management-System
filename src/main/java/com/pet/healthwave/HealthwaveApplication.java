package com.pet.healthwave;

import com.pet.healthwave.auth.AuthService;
import com.pet.healthwave.auth.AuthenticationRequest;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class HealthwaveApplication {
	public static void main(String[] args) {
		SpringApplication.run(HealthwaveApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(AuthService authService) {
		return args -> {
			var token = new AuthenticationRequest("witic43620@cumzle.com", "223abcd!");
			var token2 = new AuthenticationRequest("headPhis@cumzle.com", "223abcd!");
			var token3 = new AuthenticationRequest("dondon@cumzle.com", "223abcd!");

			System.out.println("Token1: " + authService.authenticateService(token));
			System.out.println("Token2: " + authService.authenticateService(token2));
			System.out.println("Token3: " + authService.authenticateService(token3));


		};
	}

}
