package com.example.student;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	/*
	 To kill any process listening to the port 8080:
		kill $(lsof -t -i:8080)
		
	or more violently:
		kill -9 $(lsof -t -i:8080)
	 */
}
