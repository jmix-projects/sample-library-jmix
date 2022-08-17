package com.company.library;

import com.google.common.base.Strings;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

@SpringBootApplication
public class SampleLibraryJmixApplication {

	@Autowired
	private Environment environment;

	public static void main(String[] args) {
		SpringApplication.run(SampleLibraryJmixApplication.class, args);
	}

	@Bean
	@Primary
	@ConfigurationProperties(prefix="main.datasource")
	DataSource dataSource() {
		return DataSourceBuilder.create().build();
	}

	@EventListener
	public void printApplicationUrl(ApplicationStartedEvent event) {
		LoggerFactory.getLogger(SampleLibraryJmixApplication.class).info("Application started at "
				+ "http://localhost:"
				+ environment.getProperty("local.server.port")
				+ Strings.nullToEmpty(environment.getProperty("server.servlet.context-path")));
	}
}
