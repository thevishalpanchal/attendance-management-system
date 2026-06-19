package com.attendance;

import org.springframework.boot.SpringApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableMethodSecurity
@EnableScheduling
public class AttendanceManagementSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(AttendanceManagementSystemApplication.class, args);
	}

}
