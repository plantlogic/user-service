package edu.csumb.spring19.capstone.controllers;

import edu.csumb.spring19.capstone.dto.RestDTO;
import edu.csumb.spring19.capstone.dto.RestSuccess;
import edu.csumb.spring19.capstone.dto.user.PasswordChange;
import edu.csumb.spring19.capstone.services.SelfService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/me")
@CrossOrigin(origins = "*")
public class SelfController {
    @Autowired
    private SelfService selfService;

    @Operation(summary = "Change the password of the current user.",
          security = {@SecurityRequirement(name = "Bearer")})
    @PostMapping("/changePassword")
    public RestDTO changePassword(@RequestBody PasswordChange request) throws Exception {
        return selfService.changePassword(
              request.getOldPassword(),
              request.getNewPassword()
        );
    }

    @Operation(summary = "Get a new token.",
          security = {@SecurityRequirement(name = "Bearer")})
    @GetMapping("/renewToken")
    public RestDTO renewToken() {
        return selfService.renewToken();
    }

    @Operation(summary = "Will only return 'success' if token is valid.",
          security = {@SecurityRequirement(name = "Bearer")})
    @GetMapping("/tokenValid")
    public RestDTO tokenValid() {
        return new RestSuccess();
    }
}
