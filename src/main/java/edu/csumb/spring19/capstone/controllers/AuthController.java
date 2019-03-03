package edu.csumb.spring19.capstone.controllers;

import edu.csumb.spring19.capstone.dto.RestDTO;
import edu.csumb.spring19.capstone.dto.user.UserPass;
import edu.csumb.spring19.capstone.services.AuthInterface;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    @Autowired
    private AuthInterface authService;

    @ApiOperation(value = "Sign in and issue a token for the user.")
    @PostMapping("/signin")
    public RestDTO signin(@RequestBody UserPass user) throws Throwable {
        return authService.signin(user.getUsername(), user.getPassword());
    }
}
