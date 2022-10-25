package com.example.batchsample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

 //(scanBasePackages = "com.example.batchsample.config")
//@ComponentScan("com.example.batchsample.config")
@SpringBootApplication
public class BatchsampleApplication {

	public static void main(String[] args) {
		System.out.println("STATIC VOID MAIN START!!");
		SpringApplication.run(BatchsampleApplication.class, args);
	}

}
