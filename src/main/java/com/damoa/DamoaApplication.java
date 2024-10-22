package com.damoa;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.damoa.repository.interfaces")
public class DamoaApplication {

	public static void main(String[] args) {
		SpringApplication.run(DamoaApplication.class, args);
	}

}
