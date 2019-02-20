package edu.csumb.spring19.capstone.controllers;

import edu.csumb.spring19.capstone.dto.RestDTO;
import edu.csumb.spring19.capstone.dto.RestData;
import edu.csumb.spring19.capstone.models.PLUser;
import edu.csumb.spring19.capstone.services.UserInterface;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class UserController {
    @Autowired
    private UserInterface userService;

    @ApiOperation(value = "Sign in and issue a token for the user.")
    @GetMapping("/signin")
    public RestDTO signin(@RequestParam String username, @RequestParam String password) throws Throwable {
        return userService.signin(username, password);
    }

    @ApiOperation(value = "List all user accounts.",
          authorizations = {@Authorization(value = "Bearer")})
    @GetMapping("/userlist")
    public RestDTO userList() {
        return new RestData<>(userService.allUsers());
    }
}