package edu.csumb.spring19.capstone.controllers;

import edu.csumb.spring19.capstone.dto.RestDTO;
import edu.csumb.spring19.capstone.dto.RestData;
import edu.csumb.spring19.capstone.models.PLUser;
import edu.csumb.spring19.capstone.services.UserInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class UserController {
    @Autowired
    private UserInterface userService;

    @GetMapping("/signin")
    public RestDTO signin(@RequestParam String username, @RequestParam String password) throws Exception {
        return new RestData<>(userService.signin(username, password));
    }

    @GetMapping("/userlist")
    public RestDTO userList() {
        return new RestData<Iterable<PLUser>>(userService.allUsers());
    }
}