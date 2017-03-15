package com.inin.keymanagement.exceptions;


/**
 * General bad request exception model
 */
public class BadRequestException extends RuntimeException {

    public BadRequestException(){}

    public BadRequestException(String message){super(message);}
}