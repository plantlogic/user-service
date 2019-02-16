package edu.csumb.spring19.capstone.dto;

public class RestData<E> extends RestSuccess {
    private E data;

    public RestData(E data){
        super();
        this.data = data;
    }

    public E getData(){
        return data;
    }
}
