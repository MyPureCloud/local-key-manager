package com.inin.keymanagement.exceptions;


/**
 * General Unauthorized exception model
 */
public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(){}

    public UnauthorizedException(String message){
        super(message);
    }

}
