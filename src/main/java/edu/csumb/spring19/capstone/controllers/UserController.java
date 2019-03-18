package edu.csumb.spring19.capstone.controllers;

import edu.csumb.spring19.capstone.dto.RestDTO;
import edu.csumb.spring19.capstone.dto.RestData;
import edu.csumb.spring19.capstone.dto.user.UserDTO;
import edu.csumb.spring19.capstone.dto.user.UserInfoReceive;
import edu.csumb.spring19.capstone.services.UserInterface;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/management")
@CrossOrigin(origins = "*")
@PreAuthorize("hasRole('USER_MANAGEMENT')")
public class UserController {
    @Autowired
    private UserInterface userService;

    @ApiOperation(value = "List all user accounts.",
          authorizations = {@Authorization(value = "Bearer")})
    @GetMapping("/userlist")
    public RestDTO userList() {
        return new RestData<>(userService.allUsers());
    }

    @ApiOperation(value = "Get the information for a specific user account.",
          authorizations = {@Authorization(value = "Bearer")})
    @PostMapping("/getUser")
    public RestDTO getUser(@RequestBody UserDTO user) {
        return userService.getUser(user.getUsername());
    }

    @ApiOperation(value = "Add user account.",
          authorizations = {@Authorization(value = "Bearer")})
    @PostMapping("/addUser")
    public RestDTO addUser(@RequestBody UserInfoReceive user) {
        return userService.addUser(user);
    }

    @ApiOperation(value = "Delete user account.",
          authorizations = {@Authorization(value = "Bearer")})
    @PostMapping("/deleteUser")
    public RestDTO deleteUser(@RequestBody UserDTO user) {
        return userService.deleteUser(user.getUsername());
    }
}