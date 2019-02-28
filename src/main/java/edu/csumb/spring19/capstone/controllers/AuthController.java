package edu.csumb.spring19.capstone.controllers;

import edu.csumb.spring19.capstone.dto.RestDTO;
import edu.csumb.spring19.capstone.dto.user.UserReceive;
import edu.csumb.spring19.capstone.services.UserInterface;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    @Autowired
    private UserInterface userService;

    @ApiOperation(value = "Sign in and issue a token for the user.")
    @PostMapping("/signin")
    public RestDTO signin(@RequestBody UserReceive user) throws Throwable {
        return userService.signin(user.getUsername(), user.getPassword());
    }
}
