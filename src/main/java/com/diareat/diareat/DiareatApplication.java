package com.diareat.diareat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class DiareatApplication {

	public static void main(String[] args) {
		SpringApplication.run(DiareatApplication.class, args);
	}

}
