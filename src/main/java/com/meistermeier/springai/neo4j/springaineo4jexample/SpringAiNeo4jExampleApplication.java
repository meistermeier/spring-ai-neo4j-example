package com.meistermeier.springai.neo4j.springaineo4jexample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringAiNeo4jExampleApplication {

	public static void main(String[] args) {
		SpringApplication springApplication = new SpringApplication(SpringAiNeo4jExampleApplication.class);
		springApplication.run(args);
	}

}
