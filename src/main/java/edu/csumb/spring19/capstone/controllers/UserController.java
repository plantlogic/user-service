package edu.csumb.spring19.capstone.controllers;

import edu.csumb.spring19.capstone.dto.RestDTO;
import edu.csumb.spring19.capstone.dto.RestData;
import edu.csumb.spring19.capstone.dto.user.UserDTO;
import edu.csumb.spring19.capstone.dto.user.UserInfoReceive;
import edu.csumb.spring19.capstone.dto.user.UserInfoReceiveEdit;
import edu.csumb.spring19.capstone.services.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;

import com.mongodb.util.JSON;

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

    @ApiOperation(value = "List all user accounts.",
          authorizations = {@Authorization(value = "Bearer")})
    @GetMapping("/userList")
    public RestDTO userList() {
        return new RestData<>(userService.allUsers());
    }

    @ApiOperation(value = "Get the information for a specific user account.",
          authorizations = {@Authorization(value = "Bearer")})
    @PostMapping("/getUser")
    public RestDTO getUser(@RequestBody UserDTO user, @RequestParam(required = false) boolean withNonNullPerms) {
        // Primitive boolean is false by default
        if (!withNonNullPerms)
            return userService.getUser(user.getUsername());
        else
            return userService.getUserWithoutNulledPerms(user.getUsername());
    }

    @ApiOperation(value = "Add user account.",
          authorizations = {@Authorization(value = "Bearer")})
    @PostMapping("/addUser")
    public RestDTO addUser(@RequestBody UserInfoReceive user) {
        System.out.println("Controller Received UserInfoReceive With Shipper ID: ["+user.getShipperID()+"] ");
        return userService.addUser(user);
    }

    @ApiOperation(value = "Delete user account.",
          authorizations = {@Authorization(value = "Bearer")})
    @PostMapping("/deleteUser")
    public RestDTO deleteUser(@RequestBody UserDTO user) {
        return userService.deleteUser(user.getUsername());
    }

    @ApiOperation(value = "Resets user's password - emails them a random temporary password and forces them to change it on login.",
        authorizations = {@Authorization(value = "Bearer")})
    @PostMapping("/resetPassword")
    public RestDTO resetPassword(@RequestBody UserDTO user) {
        return userService.resetPassword(user.getUsername());
    }

    @ApiOperation(value = "Overwrites values in a user's info.",
          authorizations = {@Authorization(value = "Bearer")})
    @PostMapping("/editUser")
    public RestDTO editUser(@RequestBody UserInfoReceiveEdit user) {
        return userService.editUser(user);
    }
}