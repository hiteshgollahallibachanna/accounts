package com.sail.accounts.commons.controller;

import com.sail.accounts.commons.model.Error;
import com.sail.accounts.commons.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class BaseController {

    protected ResponseEntity<Object> createResponse(Object responseContent, HttpStatus status) {
        return new ResponseEntity(responseContent, status);
    }

    protected ResponseEntity<Object> createResponse(HttpStatus status) {
        return new ResponseEntity(status);
    }


    protected ResponseEntity<Object> createEmptyResponse(HttpStatus status) {
        return new ResponseEntity(status);
    }

    public ResponseEntity<Object> createErrorResponse(HttpStatus status, String message, String errorCode, List<Error> errors) {
        ErrorResponse error = new ErrorResponse(errorCode, message, errors);
        return new ResponseEntity(error, status);
    }

}
