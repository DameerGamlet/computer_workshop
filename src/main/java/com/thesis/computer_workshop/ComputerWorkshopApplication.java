package com.thesis.computer_workshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

//

@SpringBootApplication
@EnableAsync
@ComponentScan({"com.thesis.computer_workshop.*"})
public class ComputerWorkshopApplication {

	public static void main(String[] args) {
		SpringApplication.run(ComputerWorkshopApplication.class, args);
	}

}
