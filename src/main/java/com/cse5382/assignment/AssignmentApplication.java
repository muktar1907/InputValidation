package com.cse5382.assignment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan({"com.cse5382.assignment.Controller", "com.cse5382.assignment.Model", "com.cse5382.assignment.Service", "com.cse5382.assignment.Repository,com.cse5382.assignment.Security"})
public class AssignmentApplication {

	public static void main(String[] args) {
		SpringApplication.run(AssignmentApplication.class, args);
		System.out.println("Finished startup");
	}

	
}
