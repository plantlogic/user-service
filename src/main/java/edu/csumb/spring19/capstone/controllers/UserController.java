package edu.csumb.spring19.capstone.controllers;

import edu.csumb.spring19.capstone.dto.RestDTO;
import edu.csumb.spring19.capstone.dto.RestData;
import edu.csumb.spring19.capstone.services.UserInterface;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/management")
@CrossOrigin(origins = "*")
public class UserController {
    @Autowired
    private UserInterface userService;

    @ApiOperation(value = "List all user accounts.",
          authorizations = {@Authorization(value = "Bearer")})
    @GetMapping("/userlist")
    public RestDTO userList() {
        return new RestData<>(userService.allUsers());
    }
}