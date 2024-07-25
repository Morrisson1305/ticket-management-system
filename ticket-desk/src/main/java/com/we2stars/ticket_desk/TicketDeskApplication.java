package com.we2stars.ticket_desk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@ComponentScan(basePackages = "com.we2stars.ticket_desk.utils")
public class TicketDeskApplication {

	public static void main(String[] args) {
		SpringApplication.run(TicketDeskApplication.class, args);
	}

}
