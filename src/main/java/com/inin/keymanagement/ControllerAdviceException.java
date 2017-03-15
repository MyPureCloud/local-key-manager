package com.inin.keymanagement;


import com.inin.keymanagement.exceptions.BadRequestException;
import com.inin.keymanagement.exceptions.UnauthorizedException;
import com.inin.keymanagement.models.dto.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ControllerAdviceException {

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Object> badRequest(HttpServletRequest req, Exception exception) {
        return new ResponseEntity<Object>(new ErrorMessage(exception.getMessage(), 400), HttpStatus.BAD_REQUEST);
    }


    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Object> unauthorized(HttpServletRequest req, Exception exception){
        return new ResponseEntity<Object>(new ErrorMessage(exception.getMessage(), 401), HttpStatus.UNAUTHORIZED);
    }

}
