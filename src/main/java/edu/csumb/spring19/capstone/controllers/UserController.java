package edu.csumb.spring19.capstone.controllers;

import edu.csumb.spring19.capstone.dto.RestDTO;
import edu.csumb.spring19.capstone.dto.RestData;
import edu.csumb.spring19.capstone.dto.user.UserDTO;
import edu.csumb.spring19.capstone.dto.user.UserInfoReceive;
import edu.csumb.spring19.capstone.dto.user.UserInfoReceiveEdit;
import edu.csumb.spring19.capstone.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/management")
@CrossOrigin(origins = "*")
@PreAuthorize("hasRole('USER_MANAGEMENT')")
public class UserController {
    @Autowired
    private UserService userService;

    @Operation(summary = "List all user accounts.",
          security = {@SecurityRequirement(name = "Bearer")})
    @GetMapping("/userList")
    public RestDTO userList() {
        return new RestData<>(userService.allUsers());
    }

    @Operation(summary = "Get the information for a specific user account.",
          security = {@SecurityRequirement(name = "Bearer")})
    @PostMapping("/getUser")
    public RestDTO getUser(@RequestBody UserDTO user, @RequestParam(required = false) boolean withNonNullPerms) {
        // Primitive boolean is false by default
        if (!withNonNullPerms)
            return userService.getUser(user.getUsername());
        else
            return userService.getUserWithoutNulledPerms(user.getUsername());
    }

    @Operation(summary = "Add user account.",
          security = {@SecurityRequirement(name = "Bearer")})
    @PostMapping("/addUser")
    public RestDTO addUser(@RequestBody UserInfoReceive user) {
        return userService.addUser(user);
    }

    @Operation(summary = "Delete user account.",
          security = {@SecurityRequirement(name = "Bearer")})
    @PostMapping("/deleteUser")
    public RestDTO deleteUser(@RequestBody UserDTO user) {
        return userService.deleteUser(user.getUsername());
    }

    @Operation(summary = "Resets user's password - emails them a random temporary password and forces them to change it on login.",
        security = {@SecurityRequirement(name = "Bearer")})
    @PostMapping("/resetPassword")
    public RestDTO resetPassword(@RequestBody UserDTO user) {
        return userService.resetPassword(user.getUsername());
    }

    @Operation(summary = "Overwrites values in a user's info.",
          security = {@SecurityRequirement(name = "Bearer")})
    @PostMapping("/editUser")
    public RestDTO editUser(@RequestBody UserInfoReceiveEdit user) {
        return userService.editUser(user);
    }
}