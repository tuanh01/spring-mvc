package edu.poly.TuAnhpolyshop;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import edu.poly.TuAnhpolyshop.config.StorageProperties;
import edu.poly.TuAnhpolyshop.service.StorageService;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class TuAnhPolyshopApplication {

	public static void main(String[] args) {
		SpringApplication.run(TuAnhPolyshopApplication.class, args);
	}
	@Bean
	CommandLineRunner init(StorageService storageService) {
		return (arg->{
			storageService.init();
		});
	}
}
