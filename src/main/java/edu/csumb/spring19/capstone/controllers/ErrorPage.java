package edu.csumb.spring19.capstone.controllers;

import edu.csumb.spring19.capstone.dto.RestDTO;
import edu.csumb.spring19.capstone.dto.RestFailure;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@RestController
public class ErrorPage implements ErrorController {
    private static final String PATH = "/error";

    @RequestMapping("/error")
    public RestDTO handleError(HttpServletRequest request) throws Throwable {
        return new RestFailure(((Throwable) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION)).getMessage());
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }
}
