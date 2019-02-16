package edu.csumb.spring19.capstone.controllers;

import edu.csumb.spring19.capstone.dto.RestDTO;
import edu.csumb.spring19.capstone.dto.RestData;
import edu.csumb.spring19.capstone.services.UserInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class UserController {
    @Autowired
    private UserInterface userService;

    @PostMapping("/signin")
    public RestDTO login(@RequestParam String username, @RequestParam String password) throws Exception {
        return new RestData<String>(userService.signin(username, password));
    }
}