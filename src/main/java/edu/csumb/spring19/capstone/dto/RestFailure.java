package edu.csumb.spring19.capstone.dto;

public class RestFailure extends RestDTO {
    private String error;

    public RestFailure(String error){
        super.success = false;
        this.error = error;
    }

    public String getError(){
        return error;
    }
}
