package edu.csumb.spring19.capstone.controllers;

import edu.csumb.spring19.capstone.dto.RestDTO;
import edu.csumb.spring19.capstone.dto.user.UserDTO;
import edu.csumb.spring19.capstone.dto.user.UserPass;
import edu.csumb.spring19.capstone.services.AuthService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    @Autowired
    private AuthService authService;

    @ApiOperation(value = "Sign in and issue a token for the user.")
    @PostMapping("/signIn")
    public RestDTO signIn(@RequestBody UserPass user) {
        return authService.signIn(user.getUsername(), user.getPassword());
    }

    @ApiOperation(value = "Resets user's password - emails them a random temporary password and forces them to change it on login.")
    @PostMapping("/resetPassword")
    public RestDTO resetPassword(@RequestBody UserDTO user) {
        return authService.resetPassword(user.getUsername());
    }
}
