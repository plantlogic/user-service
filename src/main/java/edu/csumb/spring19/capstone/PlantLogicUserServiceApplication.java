package edu.csumb.spring19.capstone;

import edu.csumb.spring19.capstone.models.PLUser;
import edu.csumb.spring19.capstone.services.PLUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Arrays;

@SpringBootApplication
public class PlantLogicUserServiceApplication implements CommandLineRunner {
    @Autowired
    PLUserDetails userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(PlantLogicUserServiceApplication.class, args);
	}

	@Override
	public void run(String... params) throws Exception {
		if (userService.size() < 1)
			userService.addUser("admin", passwordEncoder.encode("admin"), "Default User", "hello@plantlogic.org");
	}
}
