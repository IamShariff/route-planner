package com.metro.routeplanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class RouteplannerApplication {

	public static void main(String[] args) {
		SpringApplication.run(RouteplannerApplication.class, args);
	}

}
