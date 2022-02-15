package com.example.demo;

import brave.baggage.BaggageField;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	public BaggageField customerOrderIdBaggage() {
		return BaggageField.create("external_id");
	}
}
