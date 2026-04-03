package com.finance.dashboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableMethodSecurity
@RestController   // ← Add this
public class DashboardApplication {

	public static void main(String[] args) {
		SpringApplication.run(DashboardApplication.class, args);
		System.out.println("✅ Finance Dashboard Backend is running fine!");
	}

	// Add this root endpoint
	@GetMapping("/")
	public String home() {
		return "Finance Dashboard Backend is running successfully!\n\n" +
				"Go to Swagger UI: http://localhost:8081/swagger-ui/index.html";
	}
}