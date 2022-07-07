package com.northwest.lms.exceptions;

public class BadCredentialsException extends IllegalArgumentException{
    public BadCredentialsException(String message){
        super(message);
    }
}
