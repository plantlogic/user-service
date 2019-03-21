package edu.csumb.spring19.capstone.controllers;

import edu.csumb.spring19.capstone.dto.RestDTO;
import edu.csumb.spring19.capstone.dto.RestSuccess;
import edu.csumb.spring19.capstone.dto.user.PasswordChange;
import edu.csumb.spring19.capstone.services.SelfService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/me")
@CrossOrigin(origins = "*")
public class SelfController {
    @Autowired
    private SelfService selfService;

    @ApiOperation(value = "Change the password of the current user.",
          authorizations = {@Authorization(value = "Bearer")})
    @PostMapping("/changePassword")
    public RestDTO changePassword(@RequestBody PasswordChange request) throws Exception {
        return selfService.changePassword(
              request.getOldPassword(),
              request.getNewPassword()
        );
    }

    @ApiOperation(value = "Get a new token.",
          authorizations = {@Authorization(value = "Bearer")})
    @GetMapping("/renewToken")
    public RestDTO renewToken() {
        return selfService.renewToken();
    }

    @ApiOperation(value = "Will only return 'success' if token is valid.",
          authorizations = {@Authorization(value = "Bearer")})
    @GetMapping("/tokenValid")
    public RestDTO tokenValid() {
        return new RestSuccess();
    }
}
