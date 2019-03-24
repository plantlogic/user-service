package edu.csumb.spring19.capstone.controllers;

import edu.csumb.spring19.capstone.dto.RestDTO;
import edu.csumb.spring19.capstone.dto.RestFailure;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class ErrorPage implements ErrorController {
    private static final String PATH = "/error";

    @RequestMapping(PATH)
    public RestDTO handleError(HttpServletRequest request, HttpServletResponse response) {
        Exception error = (Exception) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
        Integer status = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        response.setStatus(200);

        if (error != null) return new RestFailure(error.getMessage());
        else if (status != null) {
            switch (status) {
                case 403:
                    return new RestFailure("You don't have permission to perform this action.");
                case 404:
                    return new RestFailure("The API call requested does not exist.");
                default:
                    //response.setStatus(status);
                    return new RestFailure(HttpStatus.valueOf(status).getReasonPhrase());
            }
        }
        else return new RestFailure("Unknown error.");
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }
}
