package edu.csumb.spring19.capstone;

import edu.csumb.spring19.capstone.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PlantLogicUserServiceApplication implements CommandLineRunner {
    @Autowired
    UserService userService;

	public static void main(String[] args) {
		SpringApplication.run(PlantLogicUserServiceApplication.class, args);
	}

	@Override
	public void run(String... params) throws Exception {
		userService.addDefaultUser();
	}
}
