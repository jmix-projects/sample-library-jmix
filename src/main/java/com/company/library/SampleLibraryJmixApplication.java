package com.company.library;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@SpringBootApplication
public class SampleLibraryJmixApplication {

	public static void main(String[] args) {
		SpringApplication.run(SampleLibraryJmixApplication.class, args);
	}

	@Bean
	@Primary
	@ConfigurationProperties(prefix="main.datasource")
	DataSource dataSource() {
		return DataSourceBuilder.create().build();
	}
}
