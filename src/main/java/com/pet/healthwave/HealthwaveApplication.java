package com.pet.healthwave;

import com.pet.healthwave.auth.AuthService;
import com.pet.healthwave.auth.AuthenticationRequest;
import com.pet.healthwave.user.Role;
import com.pet.healthwave.user.UserRepository;
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
	public CommandLineRunner commandLineRunner(AuthService authService, UserRepository userRepository) {
		return args -> {
			var token = new AuthenticationRequest("pijer98735@huizk.com", "password1");
			var token2 = new AuthenticationRequest("example1@mail.com", "password1");
			var token3 = new AuthenticationRequest("smth@mail.com", "password1");
			var token4 = new AuthenticationRequest("admin@mail.com", "password1");

			System.out.println("Token1: " + authService.authenticateService(token) + " \n"
					+ "Token2: " + authService.authenticateService(token2) + " \n"
					+ "Token3: " + authService.authenticateService(token3) + " \n"
					+ "Token4: " + authService.authenticateService(token4));

//			int res = userRepository.setAdminRole(4L, Role.ADMIN);
//			System.out.println(res);
		};
	}

}
